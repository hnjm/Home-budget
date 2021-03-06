package com.slusarzparadowski.database;

import android.content.Context;
import android.util.Log;

import com.slusarzparadowski.model.Element;
import com.slusarzparadowski.model.Category;
import com.slusarzparadowski.model.Model;
import com.slusarzparadowski.model.Settings;
import com.slusarzparadowski.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 2015-03-17.
 */
public class ModelDataSourceMySQL extends ModelDataSource {

    private final String TAG_VALUE = "response_value";
    private final String TAG_MESSAGE = "response_message";

    private final String URL_INSERT = "http://slusarzparadowskiprojekt.esy.es/insert.php";
    private final String URL_CHECK = "http://slusarzparadowskiprojekt.esy.es/check.php";
    private final String URL_GET = "http://slusarzparadowskiprojekt.esy.es/get.php";
    private final String URL_UPDATE = "http://slusarzparadowskiprojekt.esy.es/update.php";
    private final String URL_DELETE = "http://slusarzparadowskiprojekt.esy.es/delete.php";

    private JSONParser jsonParser = new JSONParser();

    // <editor-fold defaultstate="collapsed" desc="element">
    @Override
    public ArrayList<Element> getElements(long id_category) {
        ArrayList<Element> returnList = new ArrayList<>();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("get_element", String.valueOf(id_category)));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_GET, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response getElements " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            if(value == 1){
                JSONObject listId = json.getJSONObject("response_array_id");
                JSONObject listName = json.getJSONObject("response_array_name");
                JSONObject listValue = json.getJSONObject("response_array_element_value");
                JSONObject listConst = json.getJSONObject("response_array_const");
                JSONObject listDate = json.getJSONObject("response_array_date");
                if(listId.length() != listName.length()){
                    return null;
                }
                for(int i = 0; i < listId.length(); i++){
                    Log.d(getClass().getSimpleName(), "getElement Element(" + listId.getInt("id[" + i + "]") + "," + listName.getString("name[" + i + "]") + "," + listValue.getDouble("value[" + i + "]") + "," + listConst.getBoolean("const[" + i + "]") + "," + listDate.getString("date[" + i + "]") + ")");
                    returnList.add(new Element(listId.getInt("id[" + i + "]"),
                                (int)id_category,
                                listName.getString("name[" + i + "]"),
                                (float)listValue.getDouble("value[" + i + "]"),
                                listConst.getBoolean("const[" + i + "]"),
                                !listDate.getString("date[" + i + "]").equals("null") ? new LocalDate(listDate.getString("date[" + i + "]")) : null));

                }
            }
            return returnList;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "getElements"+ e.toString());
            return returnList;
        }
    }

    @Override
    public Element insertElement(Element element) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("insert_element_id_category", String.valueOf(element.getIdParent())));
        params.add(new BasicNameValuePair("insert_element_name", element.getName()));
        params.add(new BasicNameValuePair("insert_element_value", String.valueOf(element.getValue())));
        params.add(new BasicNameValuePair("insert_element_const", String.valueOf(element.isConstant())));
        if(element.getDate() != null)
            params.add(new BasicNameValuePair("insert_element_date", element.getDate().toString()));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_INSERT, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response insertElement " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            if(value == 1)
                element.setId(json.getInt("id_created_element"));
            else
                return null;
            return element;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "getElement"+ e.toString());
            return null;
        }

    }

    @Override
    public void deleteElement(Element element) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("delete_element", String.valueOf(element.getId())));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_INSERT, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response deleteElement " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "getElement"+ e.toString());
        }
    }

    @Override
    public void updateElement(Element element) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("update_element_id", String.valueOf(element.getId())));
        params.add(new BasicNameValuePair("update_element_name", element.getName()));
        params.add(new BasicNameValuePair("update_element_value", String.valueOf(element.getValue())));
        params.add(new BasicNameValuePair("update_element_const", String.valueOf(element.isConstant())));
        params.add(new BasicNameValuePair("update_element_date", String.valueOf(element.getDate())));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_UPDATE, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response updateElement " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            element.setId(Integer.valueOf(json.getString("id_created_element")));
            Log.d(String.valueOf(value), message);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "updateElement"+ e.toString());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="category">
    @Override
    public ArrayList<Category> getCategories(long id_user, String type) {
        ArrayList<Category> returnList = new ArrayList<>();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("get_category", String.valueOf(id_user)));
        params.add(new BasicNameValuePair("get_category_type", type));
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_GET, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response getCategories " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            JSONObject listId = json.getJSONObject("response_array_id");
            JSONObject listName = json.getJSONObject("response_array_name");
            if(listId.length() != listName.length()){
                return null;
            }
            for(int i = 0; i < listId.length(); i++){
                Log.d(getClass().getSimpleName(), "getList Category("+listId.getInt("id["+i+"]")+", "+ id_user +", "+listName.getString("name["+i+"]")+", "+type+")");
                returnList.add(new Category(listId.getInt("id["+i+"]"),
                                 (int)id_user,
                                 listName.getString("name["+i+"]"),
                                 type));
            }
            for(Category el : returnList){
                el.setElementList(this.getElements(el.getId()));
            }
            Log.d(String.valueOf(value), message);
            return returnList;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "getList"+ e.toString());
            return returnList;
        }
    }

    @Override
    public Category insertCategory(Category category) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("insert_category", String.valueOf(category.getIdParent())));
        params.add(new BasicNameValuePair("insert_category_name", category.getName()));
        params.add(new BasicNameValuePair("insert_category_type", category.getType()));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_INSERT, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response insertCategory " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            category.setId(json.getInt("response_id_created_category"));
            Log.d(String.valueOf(value), message);
            return category;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "insertCategory"+ e.toString());
            return null;
        }
    }

    @Override
    public void deleteCategory(Category category) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("delete_category", String.valueOf(category.getId())));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_DELETE, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response deleteCategory " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "deleteCategory"+ e.toString());
        }
    }

    @Override
    public void updateCategory(Category category) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("update_category", String.valueOf(category.getIdParent())));
        params.add(new BasicNameValuePair("update_category_name", category.getName()));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_UPDATE, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response updateCategory " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "updateCategory"+ e.toString());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="settings">
    @Override
    public Settings getSettings(long id_user) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("get_settings", String.valueOf(id_user)));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_GET, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response getSettings " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Settings settings = new Settings(json.getBoolean("response_auto_save"), json.getBoolean("response_auto_delete"), json.getBoolean("response_auto_local_save"));
            settings.setId(json.getInt("response_id"));
            settings.setIdParent((int)id_user);
            return settings;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "getSettings"+ e.toString());
            return null;
        }
    }

    @Override
    public Settings insertSettings(Settings settings) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("insert_settings", String.valueOf(settings.getIdParent())));
        params.add(new BasicNameValuePair("insert_settings_auto_delete", String.valueOf(settings.isAutoDeleting())));
        params.add(new BasicNameValuePair("insert_settings_auto_savings", String.valueOf(settings.isAutoSaving())));
        params.add(new BasicNameValuePair("insert_settings_auto_local_save", String.valueOf(settings.isAutoLocalSave())));
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_INSERT, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response insert_settings " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            settings.setId(json.getInt("response_id_created_settings"));
            Log.d(String.valueOf(value), message);
            return settings;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "insert_settings"+ e.toString());
            return null;
        }
    }

    @Override
    public void deleteSettings(Settings settings) {

    }

    @Override
    public void updateSettings(Settings settings) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("update_settings", String.valueOf(settings.getId())));
        params.add(new BasicNameValuePair("update_settings_auto_savings", String.valueOf(settings.isAutoSaving())));
        params.add(new BasicNameValuePair("update_settings_auto_delete", String.valueOf(settings.isAutoDeleting())));
        params.add(new BasicNameValuePair("update_settings_auto_local_save", String.valueOf(settings.isAutoLocalSave())));
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_UPDATE, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response updateSettings " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "getSettings"+ e.toString());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="user">
    @Override
    public User getUser(String name, String token) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("get_user", token));
        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(URL_GET, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response getUser " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
            return new User(json.getInt("response_id"), token, name,
                    (float) json.getDouble("response_savings"), new Settings());
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "getUser " + e.toString());
            return null;
        }
    }

    @Override
    public String[] getUsers() {
        return new String[0];
    }

    @Override
    public User insertUser(User user) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("insert_user", user.getToken()));
        params.add(new BasicNameValuePair("insert_user_name", user.getName()));
        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(URL_INSERT, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response insertUser " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
            user.setId(json.getInt("response_id_created_user"));
            user.getSettings().setIdParent(user.getId());
            user.setSettings(this.insertSettings(user.getSettings()));
            return user;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "insertToken" + e.toString());
            return null;
        }
    }

    @Override
    public void deleteUser(User user) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("delete_user", String.valueOf(user.getId())));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(URL_DELETE, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response deleteUser " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "deleteUser" + e.toString());
        }
    }

    @Override
    public void updateUser(User user) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("update_user", String.valueOf(user.getId())));
        params.add(new BasicNameValuePair("update_user_name", String.valueOf(user.getName())));
        params.add(new BasicNameValuePair("update_user_savings", String.valueOf(user.getSavings())));
        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(URL_UPDATE, "POST", params);

        // check log cat fro response
        Log.d(getClass().getSimpleName(), "Create Response updateUser " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            Log.d(String.valueOf(value), message);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "updateUser" + e.toString());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="model">
    @Override
    public Model getModel(String name, String token, Context context) {
        Model model = new Model(true, context);
        model.setUser(this.getUser(name, token));
        model.getUser().setSettings(this.getSettings(model.getUser().getId()));
        model.setIncome(this.getCategories(model.getUser().getId(), "INCOME"));
        model.setOutcome(this.getCategories(model.getUser().getId(), "OUTCOME"));
        model.calculateIncomeSum();
        model.calculateOutcomeSum();
        return model;
    }

    @Override
    public Model insertModel(Model model) {
        model.setUser(this.insertUser(model.getUser()));
        for(int i = 0; i < model.getOutcome().size(); i++){
            model.getOutcome().get(i).setIdParent(model.getUser().getId());
            model.getOutcome().set(i, this.insertCategory(model.getOutcome().get(i)));
            for(int j = 0 ; j < model.getOutcome().get(i).getElementList().size(); j++){
                model.getOutcome().get(i).getElementList().get(j).setIdParent(model.getOutcome().get(i).getId());
                model.getOutcome().get(i).getElementList().set(j , this.insertElement(model.getOutcome().get(i).getElementList().get(j)));
            }
        }
        for(int i = 0; i < model.getIncome().size(); i++){
            model.getIncome().get(i).setIdParent(model.getUser().getId());
            model.getIncome().set(i, this.insertCategory(model.getIncome().get(i)));
            for(int j = 0 ; j < model.getIncome().get(i).getElementList().size(); j++){
                model.getIncome().get(i).getElementList().get(j).setIdParent(model.getIncome().get(i).getId());
                model.getIncome().get(i).getElementList().set(j , this.insertElement(model.getIncome().get(i).getElementList().get(j)));
            }
        }
        return model;
    }

    @Override
    public void deleteModel(Model model) {
        this.deleteUser(model.getUser());
    }

    @Override
    public void updateModel(Model model) {
        this.updateUser(model.getUser());
        this.updateSettings(model.getUser().getSettings());
        for(int i = 0; i < model.getOutcome().size(); i++){
            for(int j = 0; j < model.getOutcome().get(i).getElementList().size(); j++){
                this.updateElement(model.getOutcome().get(i).getElementList().get(j));
            }
            this.updateCategory(model.getOutcome().get(i));
        }
        for(int i = 0; i < model.getIncome().size(); i++){
            for(int j = 0; j < model.getIncome().get(i).getElementList().size(); j++){
                this.updateElement(model.getIncome().get(i).getElementList().get(j));
            }
            this.updateCategory(model.getIncome().get(i));
        }
    }
    //</editor-fold>

    @Override
    public void open() throws SQLException {

    }

    @Override
    public void close() {

    }

    public String checkToken(String token){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("check_token", token));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(URL_CHECK, "POST", params);

        Log.d(getClass().getSimpleName(), "Create Response checkToken " + json.toString());

        // check for success tag
        try {
            int value = json.getInt(TAG_VALUE);
            String message = json.getString(TAG_MESSAGE);
            return message;
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "checkToken "+ e.toString());
            return "JSONException";
        }
    }
}