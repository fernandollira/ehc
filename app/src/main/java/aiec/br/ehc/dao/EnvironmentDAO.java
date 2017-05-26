package aiec.br.ehc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aiec.br.ehc.helper.DateHelper;
import aiec.br.ehc.model.Environment;
import aiec.br.ehc.model.Place;

/**
 * Provém a persistência de dados para os ambientes de locais
 *
 * @author Gilmar Soares <professorgilmagro@gmail.com>
 * @author Ricardo Boreto <ricardoboreto@gmail.com>
 * @since 2017-05-07
 */
public class EnvironmentDAO extends BaseDAO {
    static final protected String TABLE_NAME = "environments";

    public EnvironmentDAO(Context context) {
        super(context, TABLE_NAME);
    }

    /**
     * Permite gravar os dados do ambiente com base na instância do mesmo
     * Este método decide se deve atualizar ou inserir de maneira transparente
     *
     * @param environment instancia do local
     */
    public void save(Environment environment) {
        if (environment.getId() != null) {
            this.update(environment);
        } else {
            Long id = this.insert(environment);
            environment.setId(id.intValue());
        }
    }

    /**
     * Insere um novo ambiente
     *
     * @param environment instância do ambiente a ser gravada
     */
    public Long insert(Environment environment) {
        environment.setCreatedAt(new Date());
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, this.getContentValues(environment));
    }

    /**
     * Atualiza o ambiente
     *
     * @param environment Instância do ambiente
     */
    public void update(Environment environment) {
        environment.setModificationAt(new Date());
        String[] params = {environment.getId().toString()};
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, this.getContentValues(environment), "id = ?", params);
    }

    /**
     * Retorna o conteúdo com base na instância do ambiente
     *
     * @param environment instância do local
     * @return ContentValues
     */
    private ContentValues getContentValues(Environment environment) {
        ContentValues data = new ContentValues();

        if (environment.getId() != null) {
            data.put("id", environment.getId());
        }

        data.put("place_id", environment.getPlaceId());
        data.put("name", environment.getName());
        data.put("description", environment.getDescription());
        data.put("parameter", environment.getParameter());
        data.put("icon", environment.getIcon());
        data.put("creation_date", DateHelper.asIsoDateTime(environment.getCreatedAt()));
        data.put("modification_date", DateHelper.asIsoDateTime(environment.getModificationAt()));
        data.put("created_by", environment.getCreatedBy());
        data.put("modified_by", environment.getModifiedBy());
        return data;
    }

    /**
     * Retorna todos os registros dos ambientes contidos na tabela
     *
     * @return List<Environment>
     */
    public List<Environment> getAll() {
        Cursor c = this.fetchAll();
        List<Environment> environments = new ArrayList<>();
        while (c.moveToNext()) {
            environments.add(this.fillByCursor(c));
        }

        c.close();
        return environments;
    }

    /**
     * Retorna todos os registros dos ambientes para o local informado
     *
     * @return List<Environment>
     */
    public List<Environment> getAllFromPlace(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {place.getId().toString()};
        String sql = String.format("SELECT * FROM %s WHERE place_id = ?", TABLE_NAME);
        Cursor c = db.rawQuery(sql, params);
        List<Environment> environments = new ArrayList<>();
        while (c.moveToNext()) {
            environments.add(this.fillByCursor(c));
        }

        c.close();
        return environments;
    }

    /**
     * Retorna todos os registros de ambientes contidos na tabela
     *
     * @return Environment
     */
    public Environment getById(Integer id) {
        Cursor c = this.fetchById(id);
        Environment environment = this.fillByCursor(c);
        c.close();
        return environment;
    }

    /**
     * Cria uma instância de Environment com base nos dados oriundos de um cursor do recordset
     *
     * @param c Cursor
     * @return Ambiente
     */
    private Environment fillByCursor(Cursor c) {
        Environment environment = new Environment();
        if (c.getCount() > 0) {
            environment.setId(c.getInt(c.getColumnIndex("id")));
            environment.setPlaceId(c.getInt(c.getColumnIndex("place_id")));
            environment.setName(c.getString(c.getColumnIndex("name")));
            environment.setDescription(c.getString(c.getColumnIndex("description")));
            environment.setParameter(c.getString(c.getColumnIndex("parameter")));
            environment.setIcon(c.getString(c.getColumnIndex("icon")));
            environment.setCreatedBy(c.getString(c.getColumnIndex("created_by")));
            environment.setModifiedBy(c.getString(c.getColumnIndex("modified_by")));
            environment.setCreatedAt(DateHelper.asDate(c.getString(c.getColumnIndex("creation_date"))));
            environment.setModificationAt(DateHelper.asDate(c.getString(c.getColumnIndex("modification_date"))));
        }

        return environment;
    }

    /**
     * Dado o local, retorna a quantidade de ambientes cadastrado
     *
     * @param place_id  Código do local
     * @return  Qtde de ambientes
     */
    public int getEnvironmentCountFromPlaceId(Integer place_id)
    {
        String[] params = {place_id.toString()};
        String sql = String.format("SELECT COUNT(*) as total FROM %s WHERE place_id = ?", TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, params);

        c.moveToFirst();
        int total = c.getCount() > 0 ? c.getInt(c.getColumnIndex("total")) : 0;
        c.close();

        return total;
    }
}
