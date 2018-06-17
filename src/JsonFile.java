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

    public void generateJsonFile()
    {
        //todo: implement when the outermost is an array
        if(JsonFile.getInstance().isOutermostArray)
        {

        }
        else
        {
            System.out.println("{");
            int size = JsonFile.getInstance().content.size();
            for(int i=0; i<size; i++)
            {
                System.out.print("  ");
                JsonFile.getInstance().content.get(i).print();
                if (i != size - 1)
                {
                    System.out.print(",");
                }
                System.out.println();
            }
            System.out.println("}");
        }
    }

}
