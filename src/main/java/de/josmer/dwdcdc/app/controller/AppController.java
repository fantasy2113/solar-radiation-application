package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import org.jxls.template.SimpleExporter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


abstract class AppController extends Controller {

    final ISolRadRepository solRadRep;

    AppController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, ISolRadRepository solRadRep) {
        super(userRep, jwtToken, userBCrypt);
        this.solRadRep = solRadRep;
    }


    final void initExcelExport(HttpServletResponse response, String exportName, List<?> items, String props, List<String> headers) throws Exception {
        response.addHeader("Content-disposition", "attachment; filename=" + exportName + System.currentTimeMillis() + ".xls");
        response.setContentType("application/vnd.ms-excel");
        new SimpleExporter().gridExport(headers, items, props, response.getOutputStream());
        response.flushBuffer();
    }

    final int getDate(final String date) {
        try {
            return Integer.valueOf(date.replace("-", "").replace("#", ""));
        } catch (NumberFormatException e) {
            LOGGER.info(e.getMessage());
            return 0;
        }
    }

    final Integer getEndDate(int year) {
        return Integer.valueOf(year + "12");
    }

    final Integer getStartDate(int year) {
        return Integer.valueOf(year + "01");
    }

}
