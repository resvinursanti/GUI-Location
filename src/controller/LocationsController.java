/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.CountriesDAO;
import dao.LocationsDAO;
import entities.Countries;
import entities.Locations;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Evi
 */
public class LocationsController {

    private final LocationsDAO lcdao;
    private final CountriesDAO cdao;
    
    public LocationsController() {
        this.lcdao = new LocationsDAO();
        this.cdao = new CountriesDAO();
    }

    public void bindingAll(JTable table, String[] header) {
        bindingTabels(table, header, lcdao.getAll());
    }

    public boolean save(Short locationId, String streetAddress, String postalCode, String city, String stateProvince, String countryId, boolean isSave) {
        Locations loca = new Locations(locationId, streetAddress, postalCode, city, stateProvince);
        String[] cId = countryId.split(" ");
        loca.setCountryId((Countries) cdao.getById(cId[0]));
        if (isSave) return lcdao.insert(loca);
        return lcdao.update(loca);

        //new Countries(countryId));
    }

    public boolean delete(Short id) {
        return lcdao.delete(id);
    }
    
    public void bindingSearch(JTable table, String[] header, String category, String cari){
      //  bindingTabels(table, header, lcdao.search(category, cari));
      String search = cari;
        if (category.equalsIgnoreCase("countryId")) {
            Countries countries = (Countries) cdao.search("countryName", cari).get(0);
            search = countries.getCountryId().toString();
        }
        
     
        bindingTabels(table, header, lcdao.search(category, search));

        
    }

    private void bindingTabels(JTable tblLocation, String[] header, List<Object> datas) {
        //  List<Locations> datas = new LocationsDAO().getAll();
        // Object[][] data = new Object[datas.size()][3];
        Locations l;
        DefaultTableModel model = new DefaultTableModel(header, 0);
        int i = 1;
        for (Object data : datas) {
         l = (Locations) data;
//         String country = "";
//            if (l.getCountryId()!= null) {
//                country = l.getCountryId().getCountryName();
//            }
            Object[] data1 = {
                i++,
                l.getLocationId(),
                l.getStreetAddress(),
                l.getPostalCode(),
                l.getCity(),
                l.getStateProvince(),
                l.getCountryId().getCountryName()
            };
            model.addRow(data1);
        }
        tblLocation.setModel(model);
    }
    
    public void loadCountries(JComboBox jComboBox) {
        cdao.getAll().stream().map((object) -> (Countries) object).forEachOrdered((Countries) -> {
            jComboBox.addItem(Countries.getCountryId()+" - "
                    +Countries.getCountryName());
        });
    }
}

