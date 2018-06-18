import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Created by EvanChoo on 5/24/18.
 */

public class ArgProcess
{
    public static void main(String[] args) throws Exception
    {
        /**
         * process the args based on length
         * 1. length==1, yamlite filepath
         *
         * 2. length==2, yamlite -parse filepath, yamlite -json filepath,
         *
         * 3. length==3, yamlite -find key/arrar[index]/array[index].key filepath
         */
        switch (args.length)
        {
            case 0:
                String result = YmlProcess.isValid("/Users/EvanChoo/Desktop/sample.yml");
                if (result.equals("Valid"))
                {
                    System.out.println("Valid");
                }
                break;

            case 1:
                String result1 = YmlProcess.isValid(args[0]);
                if (result1.equals("Valid"))
                {
                    System.out.println("Valid");
                }
                break;

            case 2:
                if(args[0].equals("-parse"))
                {
                    String result21 = YmlProcess.isValid(args[1]);
                    if (result21.equals("Valid"))
                    {
                        System.out.println("Valid");
                    }

                } else if(args[0].equals("-json"))
                {
                    if(YmlProcess.isValid(args[1]).equals("Valid"))
                        JsonFile.getInstance().generateJsonFile();
                }
                break;

            case 3:
                if(YmlProcess.isValid(args[2]).equals("Valid"))
                {
                    //System.out.println(System.getProperty("user.dir"));
                    JsonFile.getInstance().find(args[1]);
                }
                break;
        }
    }
}
