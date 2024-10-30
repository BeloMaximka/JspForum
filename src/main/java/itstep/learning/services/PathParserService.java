package itstep.learning.services;

import itstep.learning.expections.HttpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class PathParserService {
    private static final String errorMsg = "Valid id is required";

    public UUID getUUIDAfterSection(HttpServletRequest req, String sectionName) throws HttpException {
        try {
            sectionName += "/";
            int index = req.getServletPath().indexOf(sectionName) + sectionName.length();
            int idExpectedLength = 36;
            return UUID.fromString(req.getServletPath().substring(index, index + idExpectedLength));
        } catch (Exception e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
        }
    }

    public Integer getIntAfterSection(HttpServletRequest req, String sectionName) throws HttpException {
        try {
            sectionName += "/";
            int index = req.getServletPath().indexOf(sectionName) + sectionName.length();
            return Integer.parseInt(req.getServletPath().substring(index));
        } catch (Exception e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
        }
    }

    public String getStringAfterSection(HttpServletRequest req, String sectionName) throws HttpException {
        try {
            sectionName += "/";
            int index = req.getServletPath().indexOf(sectionName) + sectionName.length();
            return req.getServletPath().substring(index);
        } catch (Exception e) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
        }
    }
}
