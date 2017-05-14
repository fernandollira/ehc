package aiec.br.ehc.helper;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper para conversão de datas (string -> date, date -> string)
 *
 * @author  Gilmar Soares <professorgilmagro@gmail.com>
 * @author  Ricardo Boreto <ricardoboreto@gmail.com>
 * @since   2017-05-07
 */
public class DateHelper {
    @Nullable
    public static Date asDate(String date, String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
           return null;
        }
    }

    /**
     * Converte uma string de data que esteja no formato yyyy-MM-dd
     * @param date  string a ser convertido
     *
     * @return Date
     */
    public static Date asDate(String date) {
        return DateHelper.asDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Converte um Date para uma string formatada para iso date
     * @param date  Date a ser convertido
     *
     * @return String
     */
    public static String asIsoDate(Date date) {
        if (date == null) {
            return "";
        }

        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * Converte um Date para uma string formatada para portuguese datetime
     * @param date  Date a ser convertido
     *
     * @return String
     */
    public static String asIsoDateTime(Date date) {
        if (date == null) {
            return "";
        }

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * Converte um Date para uma string formatada para portuguese date
     * @param date  Date a ser convertido
     *
     * @return String
     */
    public static String asPortugueseDate(Date date) {
        if (date == null) {
            return "";
        }

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    /**
     * Converte um Date para uma string formatada para portuguese datetime
     * @param date  Date a ser convertido
     *
     * @return String
     */
    public static String asPortugueseDateTime(Date date) {
        if (date == null) {
            return "";
        }

        Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(date);
    }
}
