package aiec.br.ehc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aiec.br.ehc.helper.DateHelper;
import aiec.br.ehc.model.Parameter;
import aiec.br.ehc.model.Resource;

/**
 * Provém a persistência de dados para os parâmetros de parâmetros
 *
 * @author Gilmar Soares <professorgilmagro@gmail.com>
 * @author Ricardo Boreto <ricardoboreto@gmail.com>
 * @since 2017-05-13
 */
public class ParameterDAO extends BaseDAO {
    static final protected String TABLE_NAME = "parameters";

    public ParameterDAO(Context context) {
        super(context, TABLE_NAME);
    }

    /**
     * Permite gravar os dados do parâmetro com base na instância do mesmo
     * Este método decide se deve atualizar ou inserir de maneira transparente
     *
     * @param parameter instancia do local
     */
    public void save(Parameter parameter) {
        if (parameter.getId() != null) {
            this.update(parameter);
        } else {
            this.insert(parameter);
        }
    }

    /**
     * Insere um novo parâmetro
     *
     * @param parameter instância do parâmetro a ser gravada
     */
    public void insert(Parameter parameter) {
        parameter.setCreatedAt(new Date());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, this.getContentValues(parameter));
    }

    /**
     * Atualiza o parâmetro
     *
     * @param parameter Instância do parâmetro
     */
    public void update(Parameter parameter) {
        parameter.setModificationAt(new Date());
        String[] params = {parameter.getId().toString()};
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, this.getContentValues(parameter), "id = ?", params);
    }

    /**
     * Retorna o conteúdo com base na instância do parâmetro
     *
     * @param parameter instância do local
     * @return ContentValues
     */
    private ContentValues getContentValues(Parameter parameter) {
        ContentValues data = new ContentValues();

        if (parameter.getId() != null) {
            data.put("id", parameter.getId());
        }

        data.put("resource_id", parameter.getResourceId());
        data.put("name", parameter.getName());
        data.put("value", parameter.getValue());
        data.put("ui_type", parameter.getUiType());
        data.put("creation_date", DateHelper.asIsoDateTime(parameter.getCreatedAt()));
        data.put("modification_date", DateHelper.asIsoDateTime(parameter.getModificationAt()));
        data.put("created_by", parameter.getCreatedBy());
        data.put("modified_by", parameter.getModifiedBy());
        return data;
    }

    /**
     * Retorna todos os registros dos locais contidos na tabela
     *
     * @return List<Parameter>
     */
    public List<Parameter> getAll() {
        Cursor c = this.fetchAll();
        List<Parameter> parameters = new ArrayList<>();
        while (c.moveToNext()) {
            parameters.add(this.fillByCursor(c));
        }

        c.close();
        return parameters;
    }

    /**
     * Retorna todos os parâmetros para o recurso informado
     *
     * @return List<Parameter>
     */
    public List<Parameter> getAllFromResource(Resource resource) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {resource.getId().toString()};
        String sql = String.format("SELECT * FROM %s WHERE resource_id = ?", TABLE_NAME);
        Cursor c = db.rawQuery(sql, params);
        List<Parameter> parameters = new ArrayList<>();
        while (c.moveToNext()) {
            parameters.add(this.fillByCursor(c));
        }

        c.close();
        return parameters;
    }

    /**
     * Retorna todos os registros de parâmetros contidos na tabela
     *
     * @return Parameter
     */
    public Parameter getById(Integer id) {
        Cursor c = this.fetchById(id);
        Parameter parameter = this.fillByCursor(c);
        c.close();
        return parameter;
    }

    /**
     * Cria uma instância de Parameter com base nos dados oriundos de um cursor do recordset
     *
     * @param c Cursor
     * @return Ambiente
     */
    private Parameter fillByCursor(Cursor c) {
        Parameter parameter = new Parameter();
        if (c.getCount() > 0) {
            parameter.setId(c.getInt(c.getColumnIndex("id")));
            parameter.setResourceId(c.getInt(c.getColumnIndex("resource_id")));
            parameter.setName(c.getString(c.getColumnIndex("name")));
            parameter.setValue(c.getString(c.getColumnIndex("value")));
            parameter.setUiType(c.getString(c.getColumnIndex("ui_type")));
            parameter.setCreatedBy(c.getString(c.getColumnIndex("created_by")));
            parameter.setModifiedBy(c.getString(c.getColumnIndex("modified_by")));
            parameter.setCreatedAt(DateHelper.asDate(c.getString(c.getColumnIndex("creation_date"))));
            parameter.setModificationAt(DateHelper.asDate(c.getString(c.getColumnIndex("modification_date"))));
        }

        return parameter;
    }

    /**
     * Dado o ambiente, retorna a quantidade de parâmetros cadastrado
     *
     * @param resourceId  Código do local
     * @return  Qtde de parâmetros
     */
    public int getParameterCountFromResourceId(Integer resourceId)
    {
        String[] params = {resourceId.toString()};
        String sql = String.format("SELECT COUNT(0) as total FROM %s WHERE resource_id = ?", TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, params);
        c.moveToFirst();

        int total = c.getCount() > 0 ? c.getInt(c.getColumnIndex("total")) : 0;
        c.close();

        return total;
    }
}
