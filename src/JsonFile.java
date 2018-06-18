import javax.naming.Name;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by EvanChoo on 5/25/18.
 */
public class JsonFile
{
    private static JsonFile singleton;

    private ArrayList<YmlItem> content;
    private boolean isOutermostArray;

    private JsonFile(){}

    public static JsonFile getInstance()
    {
        if(singleton==null)
        {
            singleton = new JsonFile();
            JsonFile.getInstance().content = new ArrayList<>();
        }

        return singleton;
    }


    public void addItem(YmlItem item)
    {
        content.add(item);
    }

    public void setIsOutermostArray(boolean outermost)
    {
        isOutermostArray = outermost;
    }

    public boolean isOutermostArray()
    {
        return isOutermostArray;
    }

    public void generateJsonFile() throws Exception
    {
        File jsonFile = new File(System.getProperty("user.dir")+"/yaml.json");
        System.out.println(System.getProperty("user.dir")+"/yaml.json");
        FileOutputStream fop = new FileOutputStream(jsonFile);

        if(JsonFile.getInstance().isOutermostArray)
        {
            //System.out.println("[");
            for(int i=0; i< JsonFile.getInstance().content.size(); i++)
            {
                JsonFile.getInstance().content.get(i).print(0, fop);

            }
            //System.out.println("]");
        }
        else
        {
            //System.out.println("{");
            fop.write("{".getBytes());
            fop.write(System.lineSeparator().getBytes());
            int size = JsonFile.getInstance().content.size();
            for(int i=0; i<size; i++)
            {
                //System.out.print("  ");
                JsonFile.getInstance().content.get(i).print(2, fop);
                if (i != size - 1)
                {
                    //System.out.print(",");
                    fop.write(",".getBytes());
                }
                //System.out.println();
                fop.write(System.lineSeparator().getBytes());
            }
            //System.out.println("}");
            fop.write("}".getBytes());
            fop.write(System.lineSeparator().getBytes());
        }
    }

    public void find(String path)
    {
        String[] paths = path.split("\\.");

        ArrayList<YmlItem> list = content;

        for(int i=0; i<paths.length; i++)
        {
            if(paths[i].contains("[")&&paths[i].contains("]")&&i!=paths.length-1)
            {
                String name = paths[i].substring(0, paths[i].indexOf('['));
                //String s= paths[i].substring(paths[i].indexOf('[')+1, paths[i].indexOf(']'));
                //int index = Integer.parseInt(s);

                boolean found = false;
                for(YmlItem item:list)
                {
                    if(item.getName().equals(name))
                    {
                        list = item.getRealValue();
                        found = true;
                    }
                }
                if (!found)
                {
                    System.out.println("null");
                    return;
                }
            }
            else if(paths[i].contains("[")&&paths[i].contains("]")&&i==paths.length-1)
            {
                String name = paths[i].substring(0, paths[i].indexOf('['));
                String s= paths[i].substring(paths[i].indexOf('[')+1, paths[i].indexOf(']'));
                int index = Integer.parseInt(s);

                boolean found = false;
                for(YmlItem item:list)
                {
                    if(item.getName().equals(name))
                    {
                        list = item.getRealValue();
                        System.out.println(list.get(index).getValue());
                        found = true;
                    }
                }
                if (!found)
                {
                    System.out.println("null");
                    return;
                }
            }
            else if(!paths[i].contains("[")&&!paths[i].contains("]")&&i==paths.length-1)
            {
                boolean found = false;
                for(YmlItem item:list)
                {
                    if(item.getName()!=null && item.getName().equals(paths[i]))
                    {
                        System.out.println(item.getValue());
                        found = true;
                    }
                }
                if (!found)
                {
                    System.out.println("null");
                    return;
                }
            }
        }
    }

}
