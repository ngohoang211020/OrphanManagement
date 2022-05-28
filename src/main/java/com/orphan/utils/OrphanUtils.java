package com.orphan.utils;

import com.google.gson.JsonObject;
import com.orphan.exception.BadRequestException;
import com.orphan.utils.constants.APIConstants;
import com.orphan.utils.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OrphanUtils {
    private static final Logger log = LoggerFactory.getLogger(OrphanUtils.class);

    /**
     * Check email in right format
     *
     * @param email email
     */
    public static void isEmail(String email) throws BadRequestException {
        String regex = Constants.EMAIL_PATTERN;
        if (!email.matches(regex)) {
            throw new BadRequestException(BadRequestException.EMAIL_IS_INVALID, APIConstants.INVALID_FORMAT_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.EMAIL), false);
        }
    }

    /**
     * Check phoneNumber in right format
     *
     * @param phoneNumber phoneNumber
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        String regex = Constants.PHONE_PATTERN;
        if (!phoneNumber.matches(regex)) {
            return false;
        }
        return true;
    }

    /**
     * Check identification in right format
     *
     * @param identification identification
     */
    public static boolean isIdentification(String identification) {
        String regex = Constants.IDENTIFICATION_PATTERN;
        if (!identification.matches(regex)) {
            return false;
        }
        return true;
    }

    /**
     * Check password in right format
     *
     * @param password password
     */
    public static boolean isPassword(String password) {
        String regex = Constants.PASS_WORD_PATTERN;
        if (!password.matches(regex)) {
            return false;
        }
        return true;
    }

    public static JsonObject getMessageListFromErrorsValidation(Errors errors) {
        JsonObject jsonObject = new JsonObject();
        try {
            for (ObjectError error : errors.getAllErrors()) {
                String fieldName = ((FieldError) error).getField();
                jsonObject.addProperty(fieldName, error.getDefaultMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Check email in right format
     *
     * @param email email
     */
    public static boolean isEmailValid(String email) {
        String regex = Constants.EMAIL_PATTERN;
        if (!email.matches(regex)) {
            return false;
        }
        return true;
    }


    /**
     * @param dateTime
     * @return
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static Date StringToDate(String str) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Wrong date format. Use dd/MM/yyyy.");
        }
        return date;
    }

    public static String DateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }
    private static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm";


    public static LocalDateTime stringToDateTime(String date, String pattern) {
        return LocalDateTime.parse(date,
                DateTimeFormatter.ofPattern(pattern));
    }
    public static LocalDateTime StringToDateTime(String str) {
        return stringToDateTime(str, DATETIME_PATTERN);

    }

    public static String DateTimeToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = date.format(formatter); // "1986-04-08 12:30"
        return formattedDateTime;
    }

    public static Integer daysBetween2Dates(Date x, Date y) {
        return Math.toIntExact((y.getTime() - x.getTime()) / (24 * 3600 * 1000));
    }


}
