package aiec.br.ehc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aiec.br.ehc.helper.DateHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Resource;

/**
 * Provém a persistência de dados para os recursos de ambientes
 *
 * @author Gilmar Soares <professorgilmagro@gmail.com>
 * @author Ricardo Boreto <ricardoboreto@gmail.com>
 * @since 2017-05-07
 */
public class ResourceDAO extends BaseDAO {
    static final protected String TABLE_NAME = "resources";

    public ResourceDAO(Context context) {
        super(context, TABLE_NAME);
    }

    /**
     * Permite gravar os dados do recurso com base na instância do mesmo
     * Este método decide se deve atualizar ou inserir de maneira transparente
     *
     * @param resource instancia do local
     */
    public void save(Resource resource) {
        if (resource.isNew()) {
            Long id = this.insert(resource);
            resource.setId(id.intValue());
        } else {
            this.update(resource);
        }
    }

    /**
     * Insere um novo recurso
     *
     * @param resource instância do recurso a ser gravada
     */
    public Long insert(Resource resource) {
        resource.setCreatedAt(new Date());
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, this.getContentValues(resource));
    }

    /**
     * Atualiza o recurso
     *
     * @param resource Instância do recurso
     */
    public void update(Resource resource) {
        resource.setModificationAt(new Date());
        String[] params = {resource.getId().toString()};
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, this.getContentValues(resource), "id = ?", params);
    }

    /**
     * Retorna o conteúdo com base na instância do recurso
     *
     * @param resource instância do local
     * @return ContentValues
     */
    private ContentValues getContentValues(Resource resource) {
        ContentValues data = new ContentValues();

        if (resource.getId() != null) {
            data.put("id", resource.getId());
        }

        data.put("environment_id", resource.getEnvironmentId());
        data.put("name", resource.getName());
        data.put("description", resource.getDescription());
        data.put("icon", resource.getIcon());
        data.put("type", resource.getType());
        data.put("method", resource.getMethod());
        data.put("state", resource.getState());
        data.put("intensity_control", resource.hasIntensityControl() ? 1 : 0);
        data.put("intensity_param", resource.getIntensityParam());
        data.put("intensity_value", resource.getIntensityValue());
        data.put("min_value", resource.getMinValue());
        data.put("max_value", resource.getMaxValue());
        data.put("step_value", resource.getStepValue());
        data.put("creation_date", DateHelper.asIsoDateTime(resource.getCreatedAt()));
        data.put("modification_date", DateHelper.asIsoDateTime(resource.getModificationAt()));
        data.put("created_by", resource.getCreatedBy());
        data.put("modified_by", resource.getModifiedBy());
        return data;
    }

    /**
     * Retorna todos os registros dos locais contidos na tabela
     *
     * @return List<Resource>
     */
    public List<Resource> getAll() {
        Cursor c = this.fetchAll();
        List<Resource> resources = new ArrayList<>();
        while (c.moveToNext()) {
            resources.add(this.fillByCursor(c));
        }

        c.close();
        return resources;
    }

    /**
     * Retorna todos os recursos disponíveis para o ambiente informado
     *
     * @return List<Resource>
     */
    public List<Resource> getAllFromEnvironment(Environment environment) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {environment.getId().toString()};
        String sql = String.format("SELECT * FROM %s WHERE environment_id = ?", TABLE_NAME);
        Cursor c = db.rawQuery(sql, params);
        List<Resource> resources = new ArrayList<>();
        while (c.moveToNext()) {
            resources.add(this.fillByCursor(c));
        }

        c.close();
        return resources;
    }

    /**
     * Retorna todos os registros de recursos contidos na tabela
     *
     * @return Resource
     */
    public Resource getById(Integer id) {
        Cursor c = this.fetchById(id);
        Resource resource = this.fillByCursor(c);
        c.close();
        return resource;
    }

    /**
     * Cria uma instância de Resource com base nos dados oriundos de um cursor do recordset
     *
     * @param c Cursor
     * @return Ambiente
     */
    private Resource fillByCursor(Cursor c) {
        Resource resource = new Resource();
        if (c.getCount() > 0) {
            resource.setId(c.getInt(c.getColumnIndex("id")));
            resource.setEnvironmentId(c.getInt(c.getColumnIndex("environment_id")));
            resource.setName(c.getString(c.getColumnIndex("name")));
            resource.setDescription(c.getString(c.getColumnIndex("description")));
            resource.setIcon(c.getString(c.getColumnIndex("icon")));
            resource.setType(c.getString(c.getColumnIndex("type")));
            resource.setState(c.getString(c.getColumnIndex("state")));
            resource.setMethod(c.getString(c.getColumnIndex("method")));
            resource.setIntensityParam(c.getString(c.getColumnIndex("intensity_param")));
            resource.setIntensityValue(c.getInt(c.getColumnIndex("intensity_value")));
            resource.setMinValue(c.getInt(c.getColumnIndex("min_value")));
            resource.setMaxValue(c.getInt(c.getColumnIndex("max_value")));
            resource.setStepValue(c.getInt(c.getColumnIndex("step_value")));
            resource.setCreatedBy(c.getString(c.getColumnIndex("created_by")));
            resource.setModifiedBy(c.getString(c.getColumnIndex("modified_by")));
            resource.setCreatedAt(DateHelper.asDate(c.getString(c.getColumnIndex("creation_date"))));
            resource.setModificationAt(DateHelper.asDate(c.getString(c.getColumnIndex("modification_date"))));

            int intensityControl = c.getInt(c.getColumnIndex("intensity_control"));
            resource.setIntensityControl(intensityControl == 1);
        }

        return resource;
    }

    /**
     * Dado o ambiente, retorna a quantidade de recursos cadastrado
     *
     * @param environmentId  Código do local
     * @return  Qtde de recursos
     */
    public int getResourceCountFromEnvironmentId(Integer environmentId)
    {
        String[] params = {environmentId.toString()};
        String sql = String.format("SELECT COUNT(0) as total FROM %s WHERE environment_id = ?", TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, params);
        c.moveToFirst();

        int total = c.getCount() > 0 ? c.getInt(c.getColumnIndex("total")) : 0;
        c.close();

        return total;
    }
}
