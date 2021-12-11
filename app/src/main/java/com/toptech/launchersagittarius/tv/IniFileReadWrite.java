package com.toptech.launchersagittarius.tv;

import android.text.format.DateFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class IniFileReadWrite {
    public static void main(String[] args) {
    }

    public static String getProfileString(String file, String section, String variable, String defaultValue) throws IOException {
        boolean isInSection = false;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while (true) {
            try {
                String strLine = bufferedReader.readLine();
                if (strLine != null) {
                    String strLine2 = strLine.trim().split("[;]")[0];
                    if (Pattern.compile("\\[\\s*.*\\s*\\]").matcher(strLine2).matches()) {
                        if (Pattern.compile("\\[\\s*" + section + "\\s*\\]").matcher(strLine2).matches()) {
                            isInSection = true;
                        } else {
                            isInSection = false;
                        }
                    }
                    if (isInSection) {
                        String strLine3 = strLine2.trim();
                        String[] strArray = strLine3.split("=");
                        if (strArray.length == 1) {
                            if (strArray[0].trim().equalsIgnoreCase(variable)) {
                                return "";
                            }
                        } else if (strArray.length == 2) {
                            if (strArray[0].trim().equalsIgnoreCase(variable)) {
                                String value = strArray[1].trim();
                                bufferedReader.close();
                                return value;
                            }
                        } else if (strArray.length > 2 && strArray[0].trim().equalsIgnoreCase(variable)) {
                            String value2 = strLine3.substring(strLine3.indexOf("=") + 1).trim();
                            bufferedReader.close();
                            return value2;
                        }
                    }
                } else {
                    bufferedReader.close();
                    return defaultValue;
                }
            } finally {
                bufferedReader.close();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean setProfileString(String file, String section, String variable, String value) throws IOException {
        String remarkStr;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        boolean isInSection = false;
        String fileContent = "";
        while (true) {
            try {
                String allLine = bufferedReader.readLine();
                if (allLine != null) {
                    String allLine2 = allLine.trim();
                    if (allLine2.split("[;]").length > 1) {
                        remarkStr = ";" + allLine2.split(";")[1];
                    } else {
                        remarkStr = "";
                    }
                    String strLine = allLine2.split(";")[0];
                    if (Pattern.compile("\\[\\s*.*\\s*\\]").matcher(strLine).matches()) {
                        if (Pattern.compile("\\[\\s*" + section + "\\s*\\]").matcher(strLine).matches()) {
                            isInSection = true;
                        } else {
                            isInSection = false;
                        }
                    }
                    if (isInSection) {
                        String getValue = strLine.trim().split("=")[0].trim();
                        if (getValue.equalsIgnoreCase(variable)) {
                            String fileContent2 = fileContent + (getValue + " = " + value + "; " + remarkStr) + "\r\n";
                            while (true) {
                                String allLine3 = bufferedReader.readLine();
                                if (allLine3 != null) {
                                    fileContent2 = fileContent2 + allLine3 + "\r\n";
                                } else {
                                    bufferedReader.close();
                                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
                                    bufferedWriter.write(fileContent2);
                                    bufferedWriter.flush();
                                    bufferedWriter.close();
                                    bufferedReader.close();
                                    return true;
                                }
                            }
                        }
                    }
                    fileContent = fileContent + allLine2 + "\r\n";
                } else {
                    bufferedReader.close();
                    return false;
                }
            } catch (Throwable th) {
                bufferedReader.close();
                throw th;
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean setProfileString(String file, String section, List<Map<String, String>> list) throws IOException {
        String remarkStr;
        String remarkStr2;
        String time = DateFormat.format("yyyy-MM-dd", new Date()).toString();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        boolean isInSection = false;
        String fileContent = "";
        while (true) {
            try {
                String allLine = bufferedReader.readLine();
                if (allLine != null) {
                    String allLine2 = allLine.trim();
                    if (allLine2.split("[;]").length > 1) {
                        remarkStr = ";" + allLine2.split(";")[1];
                    } else {
                        remarkStr = "";
                    }
                    String strLine = allLine2.split(";")[0];
                    if (Pattern.compile("\\[\\s*.*\\s*\\]").matcher(strLine).matches()) {
                        if (Pattern.compile("\\[\\s*" + section + "\\s*\\]").matcher(strLine).matches()) {
                            isInSection = true;
                        } else {
                            isInSection = false;
                        }
                    }
                    if (isInSection) {
                        String strLine2 = strLine.trim();
                        String getValue = strLine2.split("=")[0].trim();
                        if (getValue.equalsIgnoreCase(list.get(0).get("id"))) {
                            String fileContent2 = fileContent + (getValue + " = " + list.get(0).get("val") + "; " + "#modify@" + time + remarkStr) + "\r\n";
                            int i = 1;
                            while (true) {
                                String allLine3 = bufferedReader.readLine();
                                if (allLine3 == null) {
                                    bufferedReader.close();
                                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
                                    bufferedWriter.write(fileContent2);
                                    bufferedWriter.flush();
                                    bufferedWriter.close();
                                    bufferedReader.close();
                                    return true;
                                } else if (i < list.size()) {
                                    if (allLine3.split("[;]").length > 1) {
                                        remarkStr2 = ";" + allLine3.split(";")[1];
                                    } else {
                                        remarkStr2 = "";
                                    }
                                    if (allLine3.split(";") != null && allLine3.split(";").length > 0) {
                                        strLine2 = allLine3.split(";")[0];
                                    }
                                    strLine2 = strLine2.trim();
                                    if (strLine2.split("=") != null && strLine2.split("=").length > 0) {
                                        getValue = strLine2.split("=")[0].trim();
                                    }
                                    if (getValue.equalsIgnoreCase(list.get(i).get("id"))) {
                                        fileContent2 = fileContent2 + (getValue + " = " + list.get(i).get("val") + "; " + "#modify@" + time + remarkStr2) + "\r\n";
                                        i++;
                                    } else {
                                        fileContent2 = fileContent2 + allLine3 + "\r\n";
                                    }
                                } else {
                                    fileContent2 = fileContent2 + allLine3 + "\r\n";
                                }
                            }
                        }
                    }
                    fileContent = fileContent + allLine2 + "\r\n";
                } else {
                    bufferedReader.close();
                    return false;
                }
            } catch (Throwable th) {
                bufferedReader.close();
                throw th;
            }
        }
    }
}
