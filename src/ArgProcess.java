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
                YmlProcess.isValid("/Users/EvanChoo/Desktop/sample.yml");
                break;

            case 1:
                YmlProcess.isValid(args[0]);
                break;

            case 2:
                if(args[0].equals("-parse"))
                {
                    YmlProcess.isValid(args[1]);

                } else if(args[0].equals("-json"))
                {

                }
                break;

            case 3:

                break;
        }
    }
}
