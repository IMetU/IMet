package com.example.imetu.imet.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Address {
    JSONArray address_components;
    JSONArray types;
    String address_types;
    String street_number;
    String route;
    String area_level_4;
    String area_level_3;
    String area_level_2;
    String area_level_1;
    String country;
    String postal_code;

    public Address(JSONObject object){
        try{
            address_components = object.getJSONArray("address_components");
            for(int i= 0; i < address_components.length(); i++){
                types = address_components.getJSONObject(i).getJSONArray("types");
                address_types = types.getString(0);
                switch (address_types){
                    case "street_number":
                        street_number = address_components.getJSONObject(i).getString("long_name");
                        break;
                    case "route":
                        route = address_components.getJSONObject(i).getString("long_name");
                        break;
                    case "administrative_area_level_4":
                        area_level_4 = address_components.getJSONObject(i).getString("long_name");
                        break;
                    case "administrative_area_level_3":
                        area_level_3 = address_components.getJSONObject(i).getString("long_name");
                        break;
                    case "administrative_area_level_2":
                        area_level_2 = address_components.getJSONObject(i).getString("long_name");
                        break;
                    case "administrative_area_level_1":
                        area_level_1 = address_components.getJSONObject(i).getString("long_name");
                        break;
                    case "country":
                        country = address_components.getJSONObject(i).getString("long_name");
                        break;
                    case "postal_code":
                        postal_code = address_components.getJSONObject(i).getString("long_name");
                        break;
                    default :
                        break;
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getStreet_number() {
        return street_number;
    }

    public String getRoute() {
        return route;
    }

    public String getArea_level_4() {
        return area_level_4;
    }

    public String getArea_level_3() {
        return area_level_3;
    }

    public String getArea_level_2() {
        return area_level_2;
    }

    public String getArea_level_1() {
        return area_level_1;
    }

    public String getCountry() {
        return country;
    }

    public String getPostal_code() {
        return postal_code;
    }
}
