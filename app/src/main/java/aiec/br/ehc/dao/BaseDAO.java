package aiec.br.ehc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import aiec.br.ehc.helper.ManifestHelper;
import aiec.br.ehc.model.Place;

/**
 * Centraliza os metodos e recursos comuns da parte de persistência de dados
 *
 * @author Gilmar Soares <professorgilmagro@gmail.com>
 * @author Ricardo Boreto <ricardoboreto@gmail.com>
 * @since 2017-05-07
 */
abstract class BaseDAO extends SQLiteOpenHelper {
    private final String tableName;

    public BaseDAO(Context context, String tableName) {
        super(
                context,
                ManifestHelper.from(context).getMetadata("DATABASE").toString(),
                null,
                (int) ManifestHelper.from(context).getMetadata("VERSION")
        );

        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.createTablePlaces(sqLiteDatabase);
        this.createTableEnvironment(sqLiteDatabase);
        this.createTableResource(sqLiteDatabase);
        this.createTableParameter(sqLiteDatabase);
        this.createIndexes(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlaceDAO.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EnvironmentDAO.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ResourceDAO.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ParameterDAO.TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    /**
     * Cria a tabela de locais
     *
     * @param db    Instância do banco de dados
     */
    private void createTablePlaces(SQLiteDatabase db)
    {
        String sql = String.format(
                "CREATE TABLE %s(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "protocol VARCHAR(20)," +
                    "host VARCHAR(50)," +
                    "port INTEGER default 80," +
                    "authorization_type VARCHAR(40)," +
                    "access_token VARCHAR(200)," +
                    "token_send_method VARCHAR(20)," +
                    "token_flag VARCHAR(20) DEFAULT 'token'," +
                    "user_credentials VARCHAR(300)," +
                    "credential_flag VARCHAR(20) DEFAULT 'Basic'," +
                    "name VARCHAR(100)," +
                    "description TEXT," +
                    "icon TEXT," +
                    "creation_date TEXT," +
                    "modification_date TEXT," +
                    "created_by TEXT," +
                    "modified_by TEXT" +
                ")",
                PlaceDAO.TABLE_NAME
        );

        db.execSQL(sql);
    }

    /**
     * Cria a tabela de ambientes
     *
     * @param db    Instância do banco de dados
     */
    private void createTableEnvironment(SQLiteDatabase db)
    {
        String sql = String.format(
                "CREATE TABLE %s(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "place_id INTEGER NOT NULL," +
                    "name VARCHAR(100)," +
                    "description TEXT," +
                    "parameter TEXT," +
                    "icon TEXT," +
                    "creation_date TEXT," +
                    "modification_date TEXT," +
                    "created_by TEXT," +
                    "modified_by TEXT" +
                ")",
                EnvironmentDAO.TABLE_NAME
        );

        db.execSQL(sql);
    }

    /**
     * Retorna a declaração DDL para criação da tabela de ambientes
     *
     * @param db    Instância do banco de dados
     */
    private void createTableResource(SQLiteDatabase db)
    {
        String sql = String.format(
                "CREATE TABLE %s(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "environment_id INTEGER NOT NULL," +
                    "icon TEXT," +
                    "name VARCHAR(100)," +
                    "description TEXT," +
                    "type VARCHAR(50) NOT NULL DEFAULT 'switch'," +
                    "state VARCHAR(50) NOT NULL DEFAULT 'off'," +
                    "intensity_control INTEGER NOT NULL DEFAULT 0," +
                    "intensity_param VARCHAR(50)," +
                    "intensity_value INTEGER," +
                    "method TEXT NOT NULL DEFAULT 'GET'," +
                    "read_format VARCHAR(10)," +
                    "read_node VARCHAR(100)," +
                    "min_value INTEGER," +
                    "max_value INTEGER," +
                    "step_value INTEGER," +
                    "creation_date TEXT," +
                    "modification_date TEXT," +
                    "created_by TEXT," +
                    "modified_by TEXT" +
                ")",
                ResourceDAO.TABLE_NAME
        );

        db.execSQL(sql);
    }

    /**
     * Cria a tabela de parâmetros
     *
     * @param db    Instância do banco de dados
     */
    private void createTableParameter(SQLiteDatabase db)
    {
        String sql = String.format(
                "CREATE TABLE %s(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "resource_id INTEGER NOT NULL," +
                    "action TEXT," +
                    "name VARCHAR(100)," +
                    "value TEXT," +
                    "creation_date TEXT," +
                    "modification_date TEXT," +
                    "created_by TEXT," +
                    "modified_by TEXT" +
                ")",
                ParameterDAO.TABLE_NAME
        );

        db.execSQL(sql);
    }

    /**
     * Cria os indices das tabelas
     *
     * @param db    Instância do banco de dados
     */
    private void createIndexes(SQLiteDatabase db)
    {
        db.execSQL("CREATE INDEX idx_place_id ON environments (place_id)");
        db.execSQL("CREATE INDEX idx_environment_id ON resources (environment_id)");
        db.execSQL("CREATE INDEX idx_resource_id ON parameters (resource_id)");
    }

    /**
     * Retorna todos os registros da tabela
     *
     * @return devolve um cursor
     */
    public Cursor fetchAll() {
        String sql = String.format("SELECT * FROM %s", this.tableName);
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    /**
     * Localiza um registro com o id especificado
     *
     * @return Cursor
     */
    public Cursor fetchById(Integer id) {
        String[] params = {id.toString()};
        String sql = String.format("SELECT * FROM %s WHERE id = ?", this.tableName);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, params);
        cursor.moveToFirst();
        return cursor;
    }

    /**
     * Permite excluir um registro com o id informado
     *
     * @param id    ID do registro a ser excluido
     */
    public void delete(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {id.toString()};
        db.delete(this.tableName, "id = ?", params);
    }
}
