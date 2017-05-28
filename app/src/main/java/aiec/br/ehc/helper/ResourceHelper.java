package aiec.br.ehc.helper;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ResourceHelper {
    private Context context;

    public ResourceHelper(Context context) {
        this.context = context;
    }

    /**
     * Retorna uma instância deste objeto, facilitando a chamada aos métodos da classe
     * @param context   Contexto
     * @return self
     */
    public static ResourceHelper from(Context context) {
        return new ResourceHelper(context);
    }

    /**
     * Retorna o ResourceID para o Drawable name informado
     * @param name  Nome do drawable
     *
     * @return int
     */
    public int getIdentifierFromDrawable(String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    /**
     * Com base numa lista adaptada exclusivamente para este App, retorna uma lista simples
     * para o campo especificado em key, de uma lista parametrizada
     *
     * @param key   Campo chave utilizado para iteração da lista
     * @return array
     */
    public String[] getSimpleList(String[] images, String key) {
        List<String> normalizedList = new ArrayList<>();
        for (String image : images) {
            String[] items = image.split(";");
            for (String item : items) {
                String[] params = item.split(":");
                if (params[0].equals(key)) {
                    normalizedList.add(params[1]);
                    break;
                }
            }
        }

        return normalizedList.toArray(new String[normalizedList.size()]);
    }

    /**
     * Retorna o valor para um determinado parâmetro de um item de imagem parametrizada
     * @param raw   Valores parametrizados
     * @param key   Campo a ser retornado
     *
     * @return string
     */
    public String getValueFrom(String raw, String key) {
        if (raw.contains(";")) {
            String[] items = raw.split(";");
            for (String item : items) {
                String[] params = item.split(":");
                if (params[0].equals(key)){
                    return params[1];
                }
            }
        }

        return null;
    }

    /**
     * Retorna o valor para um determinado parâmetro de um item de imagem parametrizada
     * @param rawProperties   Valores parametrizados
     *
     * @return Propriedades encapsuladas no pacote
     */
    public Bundle getProperties(String rawProperties) {
        Bundle properties = new Bundle();
        if (rawProperties.contains(";")) {
            String[] items = rawProperties.split(";");
            for (String item : items) {
                String[] params = item.split(":");
                properties.putString(params[0], params[1]);
            }
        }

        return properties;
    }

    /**
     * Dado a imagem, retorna os parâmetros da mesma
     *
     * @return Propriedades encapsuladas no pacote
     */
    public Bundle getPropertiesByDrawable(String[] images, String drawable) {
        Bundle properties = new Bundle();
        for (String image : images) {
            if (image.contains(":" + drawable + ";")) {
                return this.getProperties(image);
            }
        }
        return properties;
    }
}