import java.io.*;

/**
 * Created by EvanChoo on 5/24/18.
 */
public class FileUtil
{
    public static BufferedReader findFile(String filepath) throws IOException
    {
        File file = new File(filepath);
        if (file.exists())
        {
            if (!file.isDirectory())
            {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                return br;
            }
            else
            {
                throw new IOException("Input path is a directory");
            }
        }
        else
        {
            throw new FileNotFoundException(filepath);
        }
    }

    public static LineNumberReader lineNumberReader(String filepath) throws IOException
    {
        File file = new File(filepath);
        if (file.exists())
        {
            if(!file.isDirectory())
            {
                LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(filepath));
                return lineNumberReader;
            }
            else
            {
                throw new IOException("Input path is a diretory");
            }
        }
        else
        {
            throw new FileNotFoundException(filepath);
        }
    }
}
