package sample.source;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class DatabaseHandler  {

    PreparedStatement prSt = null;
    Statement st;


    public void signUpUser(User user)  {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USERS_FIRSTNAME + "," + Const.USERS_LASTNAME + "," +
                Const.USERS_USERNAME + "," + Const.USERS_PASSWORD + "," +
                Const.USERS_LOCATION + "," + Const.USERS_GENDER + ")" +
                "VALUES(?,?,?,?,?,?)";
        try {
        prSt = DBConnection.getInstance().getDbConnection().prepareStatement(insert);
        prSt.setString(1, user.getFirstName());
        prSt.setString(2, user.getLastName());
        prSt.setString(3, user.getUserName());
        prSt.setString(4, user.getPassword());
        prSt.setString(5, user.getLocation());
        prSt.setString(6, user.getGender());

        prSt.executeUpdate();

        String getid = "SELECT * FROM " + Const.USER_TABLE +
                " ORDER BY id DESC LIMIT 1";

        st = DBConnection.getInstance().getDbConnection().createStatement();
        ResultSet res = st.executeQuery(getid);
        String id="";
        if (res.next()) {
            id = String.valueOf(res.getInt("id"));
            user.setId(Integer.valueOf(id));
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("file.xml"))  ;

        Element mainRoot =  document.getDocumentElement();
        Element root = document.createElement("user");
            mainRoot.appendChild(root);
            root.setAttribute("id", id);
        Element name =  document.createElement("name");
            Text nameText = document.createTextNode(user.getFirstName());
            root.appendChild(name);
            name.appendChild(nameText);
            Element lastName = document.createElement("lastname");
            Text lastNameText = document.createTextNode(user.getLastName());
            root.appendChild(lastName);
            lastName.appendChild(lastNameText);
            Element login = document.createElement("username");
            Text loginText = document.createTextNode(user.getUserName());
            root.appendChild(login);
            login.appendChild(loginText);
            Element pass = document.createElement("password");
            Text passText = document.createTextNode(user.getPassword());
            root.appendChild(pass);
            pass.appendChild(passText);
            Element local = document.createElement("location");
            Text localText = document.createTextNode(user.getLocation());
            root.appendChild(local);
            local.appendChild(localText);
        Element dateRegister = document.createElement("date");
            String date = new Date().toString();
            Text dateText = document.createTextNode(date);
            root.appendChild(dateRegister);
            dateRegister.appendChild(dateText);

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("file.xml")));

        } catch (SQLException | ParserConfigurationException |
                TransformerException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getDbConnection().prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            resSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resSet;
    }

    public void removeUser (User user) throws SQLException, ParserConfigurationException, IOException, SAXException, TransformerException {
            String deleteQuery = "DELETE FROM " + Const.USER_TABLE
                    + " WHERE " + Const.USERS_ID + " = " + user.getId();

            int index = user.getId();

            st = DBConnection.getInstance().getDbConnection().createStatement();
            st.executeUpdate(deleteQuery);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("file.xml"));

            NodeList nodes = document.getElementsByTagName("user");
            Element root = document.getDocumentElement();
            System.out.println(document.getElementsByTagName("user").getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            if (Integer.valueOf(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue()) == (index)){
                root.removeChild(nodes.item(i));
            }
        }

        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("file.xml"));
        transformer.transform(source, result);
    }
}