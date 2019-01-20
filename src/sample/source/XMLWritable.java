package sample.source;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class XMLWritable implements IWriter {
    public void signUpUser(User user) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("file.xml"));

            Element mainRoot = document.getDocumentElement();
            Element root = document.createElement("user");
            mainRoot.appendChild(root);
            root.setAttribute("id", String.valueOf(user.getId()));
            Element name = document.createElement("name");
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

        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUser(User user) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("file.xml"));

            NodeList nodes = document.getElementsByTagName("user");
            Element root = document.getDocumentElement();

            for (int i = 0; i < nodes.getLength(); i++) {
                if (Integer.valueOf(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue()) == (user.getId())) {
                    root.removeChild(nodes.item(i));
                }
            }

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("file.xml"));
            transformer.transform(source, result);
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
