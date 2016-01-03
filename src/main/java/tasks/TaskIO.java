package tasks;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * methods of writting and reading lists tasks
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
public class TaskIO{

    public static void write(TaskList tasks, OutputStream out) throws IOException {
        DataOutputStream outStream = new DataOutputStream(out);
        outStream.writeInt(tasks.size());                           //size of all tasks

        for (Task task : tasks) {
            int lt = task.getTitle().length();
            outStream.writeInt(task.getTitle().length());

            for (int i =0; i<lt ;i++) {
                outStream.writeChar(task.getTitle().charAt(i));     //write task Title
            }

            //active
            outStream.writeBoolean(task.isActive());

            //interval
            int interv = task.getRepeatInterval();
            outStream.writeInt(interv);

            // repeat or not
            if (interv == 0){                                        //task not repeat
                outStream.writeLong(task.getStartTime().getTime());
            } else {                                                 // if task repeat write start and and
                outStream.writeLong(task.getStartTime().getTime());
                outStream.writeLong(task.getEndTime().getTime());
            }
        }

    }

    public static void read(TaskList tasks, InputStream in) throws Exception {
        DataInputStream inStream = new DataInputStream(in);
        int len = inStream.readInt();                               //length of array tasks

        for (int i = 0; i < len; i++) {
            //for Task title
            StringBuilder nt = new StringBuilder();
            int lt = inStream.readInt();                            // length of title

            for (int ii = 0; ii<lt ;ii++) {
                nt.append(inStream.readChar());
            }

            boolean active = inStream.readBoolean();
            int     interv = inStream.readInt();
            Date    date = new Date(inStream.readLong());
            Task    t;

            if (interv == 0){                                       //task not repeat
                t = new Task(nt.toString(), date);
                t.setActive(active);
            } else {
                Date dateEnd = new Date(inStream.readLong());
                t = new Task(nt.toString(), date, dateEnd, interv);
                t.setActive(active);
            }
            tasks.add(t);
        }

    }

    public static void writeBinary(TaskList tasks, File file){
        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            write(tasks, dos);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void readBinary(TaskList tasks, File file){
        try(DataInputStream dos = new DataInputStream(new FileInputStream(file))) {
            read(tasks, dos);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    public static void write(TaskList tasks, Writer out){
        BufferedWriter   bufOut = null;
        SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try {
            bufOut = new BufferedWriter(out);
            int lenArr = tasks.size();

            for (Task t : tasks){
                bufOut.append("\"").append(t.getTitle()).append("\"");

                if (!t.isRepeated()) {                                                      // if task repeat
                    bufOut.append(" at [").append(dateForm.format(t.getTime())).append("]");
                } else {                                                                    // if task not repeat
                    bufOut.append(" from [").append(dateForm.format(t.getStartTime())).append("]");
                    bufOut.append(" to [").append(dateForm.format(t.getEndTime())).append("]");
                    bufOut.append(" every ");

                    int     interOld = t.getRepeatInterval();
                    int     interNew;
                    boolean flag = false;                                       // for space between days, hours...
                    bufOut.append("[");

                    // days
                    if (interOld >= 86400){
                        flag = true;

                        interNew = interOld / 86400; //the integer part of the division
                        if (interNew == 1) bufOut.append("1 day");
                        else bufOut.append(interNew + " day");

                        interOld = interOld - (interNew * 86400);
                    }

                    //hours
                    if (interOld >= 3600){
                        if (flag) {
                            bufOut.append(" "); //If a previous condition is do, then put a space between them
                        } else {
                            flag = true;
                        }

                        interNew = interOld / 3600;
                        if (interNew == 1) {
                            bufOut.append("1 hour");
                        } else {
                            bufOut.append(interNew + " hours");
                        }
                        interOld = interOld - (interNew * 3600);
                    }

                    //minute
                    if (interOld >= 60){
                        if (flag)  {
                            bufOut.append(" ");
                        } else {
                            flag = true;
                        }

                        interNew = interOld / 60;
                        if (interNew == 1) {
                            bufOut.append("1 minute");
                        }
                        else {
                            bufOut.append(interNew + " minutes");
                        }
                        interOld = interOld - (interNew * 60);
                    }

                    //second
                    if (interOld > 0){
                        if (flag)  {
                            bufOut.append(" ");
                        }
                        if (interOld == 1) {
                            bufOut.append("1 second");
                        } else {
                            bufOut.append(interOld + " seconds");
                        }
                    }
                    bufOut.append("]");
                }

                // if inactive
                if(!t.isActive()){
                    bufOut.append(" inactive");
                }

                if (lenArr > 1) {
                    bufOut.append(";");
                } else {
                    bufOut.append(".");
                }
                lenArr--;
                bufOut.append("\r\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufOut != null) {
                    bufOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void read(TaskList tasks, Reader in){
        BufferedReader   bufIn = null;
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try{
            bufIn = new BufferedReader(in);
            String  s;
            int     len;
            Task    task;
            boolean active;

            while ((s = bufIn.readLine()) !=null ){
                // divide the first row to select the name of the task
                String[] newStrTask = s.split("\"");
                String nameTask = newStrTask[1];                                    // it is title

                //divide another part
                String[] newStr = newStrTask[2].split(" ");
                newStrTask = null;

                // first element in array is " "? , because delete it
                System.arraycopy(newStr, 1, newStr, 0, newStr.length-1);

                //not repeat
                if (newStr[0].equals("at")) {
                    newStr[1] = newStr[1].substring(1,  newStr[1].length());        //delete [ in string

                    if(newStr[3].equals("inactive;") || newStr[3].equals("inactive.")) {
                        active = false;
                        newStr[2] = newStr[2].substring(0, newStr[2].length() - 1); //without ]
                    } else {
                        newStr[2] = newStr[2].substring(0,  newStr[2].length() - 2);//without ]; or ].
                        active = true;
                    }

                    //create task
                    task = new Task(nameTask, date.parse(newStr[1] +" "+ newStr[2]));
                    task.setActive(active);
                    tasks.add(task);

                }else if (newStr[0].equals("from")) {
                    newStr[1] = newStr[1].substring(1,  newStr[1].length());        //delete [ in string
                    newStr[2] = newStr[2].substring(0, newStr[2].length() - 1);     //delete ] in string
                    Date start = date.parse(newStr[1] +" "+ newStr[2]);

                    //newStr[3] is to
                    newStr[4] = newStr[4].substring(1, newStr[4].length());         //delete [ in string
                    newStr[5] = newStr[5].substring(0, newStr[5].length() - 1);     //delete ] in string
                    Date end = date.parse(newStr[4] +" "+ newStr[5]);

                    // newStr[6] is every
                    newStr[7] = newStr[7].substring(1, newStr[7].length());         //delete [

                    // inactive or not, and delete ] before string="inactive"
                    len = newStr.length-2;

                    if (newStr[len].equals("inactive;") || newStr[len].equals("inactive.")) {
                        len = len -1;
                        active = false;
                        newStr[len] = newStr[len].substring(0, newStr[len].length() - 1);//delete ]
                    } else {
                        //if task active delete ]; or ].
                        newStr[len] = newStr[len].substring(0, newStr[len].length() - 2);
                        active = true;
                    }

                    int interval = 0;

                    for (int i = 7; i <= len - 1; i=i+2) {
                        if (newStr[i+1].equals("day") || newStr[i+1].equals("days"))
                            interval = interval +  Integer.parseInt(newStr[i]) * 86400;
                        if (newStr[i+1].equals("hour") || newStr[i+1].equals("hours"))
                            interval = interval + Integer.parseInt(newStr[i]) * 3600;
                        if (newStr[i+1].equals("minute") || newStr[i+1].equals("minutes"))
                            interval = interval + Integer.parseInt(newStr[i]) * 60;
                        if (newStr[i+1].equals("second") || newStr[i+1].equals("seconds"))
                            interval = interval + Integer.parseInt(newStr[i]);
                    }

                    //create task
                    task = new Task(nameTask, start, end, interval);
                    task.setActive(active);
                    tasks.add(task);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufIn != null)
                    bufIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeText(TaskList tasks, File file){
        try {
            write(tasks, new FileWriter(file));
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static void readText(TaskList tasks, File file){
        try {
            read(tasks, new FileReader(file) );
        } catch (IOException e) {
            e.getMessage();
        }

    }


    static DocumentBuilder builder;
    /**
     * method creates factory for work with XML
     */
    public static void paramLangXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            Model.log.error("ParamLangXML");
        }
    }

    /**
     * write in XML current time and file name with tasks
     *
     * @param s is name of file
     * @throws TransformerException
     * @throws IOException
     */
    public static void writeXML(String s) throws TransformerException, IOException{
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        paramLangXML();
        Document doc = builder.newDocument();
        Element RootElement = doc.createElement("Preference");

        //time last work
        Element NameElementTitle = doc.createElement("timeLastWork");
        NameElementTitle.appendChild(doc.createTextNode(date.format(System.currentTimeMillis())));
        RootElement.appendChild(NameElementTitle);

        //file name
        Element ElementTitle = doc.createElement("nameFileSaveTask");
        ElementTitle.appendChild(doc.createTextNode(s));
        RootElement.appendChild(ElementTitle);

        // add in XML
        doc.appendChild(RootElement);
        Transformer t=  TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("Preference.xml")));
        //DOMSource -- представляет полученные данные в виде Document Object Model (DOM).
        //(представляется в виде древовидной структуры документа)
        //StreamResult -- "Записывает в память" преобразованный документ... к которому мы можем уже обращаться...
        //как к xml документу. А файл, мы в него выгружаем полученный результат (в StreamResult) конечного преобразования.
        //transform - метод который и позволяет преобразовывать "исходник" (текст который мы выдаём за xml)
        //в xml (древовидную структуру)
    }

    /**
     * for read XML
     *
     * @return time is work program in the previous time
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     */
    public static Date readXML() throws IOException, SAXException, ParseException {
        Date             date;
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        paramLangXML();
        Document document = builder.parse(new File("Preference.xml"));
        document.getDocumentElement().normalize();

        //take time
        NodeList nList = document.getElementsByTagName("timeLastWork");
        Node node = nList.item(0);
        date = simpleDate.parse(node.getTextContent());

        //take name of file
        nList = document.getElementsByTagName("nameFileSaveTask");
        node = nList.item(0);
        Model.filename = node.getTextContent();

        return date;
    }
}
