/**
 * 
 */

/**
 * @author pillybuilt
 *
 */
import java.io.*; 
import java.net.*; 
import java.util.*; 
import java.util.Scanner; 
public class ExamViewDecoderJava {

	/**
	 * @param args
	 */
	static String quiz_url = ""; 
	//static String quiz_source = ""; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean containsURL = false; 
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].startsWith("-url="))
			{
				containsURL = true; 
				quiz_url = args[i].substring(5);
				System.out.println("URL set to " + quiz_url); 
				try {
					printAnswerPairs(getUrlSource(quiz_url)); 
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			if (args[i].startsWith("-src="))
			{
				containsURL = true;
				printAnswerPairs(readSrc(args[i].substring(5))); 
			}
			if (args[i].startsWith("-h"))
			{
				printHelp();
				return; 
			}
		}
		if (!containsURL)
		{
			System.out.println("Enter URL:");
			Scanner scan = new Scanner(System.in);
			quiz_url = scan.nextLine(); 
			System.out.println("URL set to " + quiz_url);
			try { 
				printAnswerPairs(getUrlSource(quiz_url)); 
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
	}
	public static String readSrc(String path)
	{
		File file = new File(path); 
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
 
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
 
            // repeat until all lines is read
            while ((text = reader.readLine()) != null) {
                contents.append(text)
                        .append(System.getProperty(
                                "line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return contents.toString(); 
	}
	public static void printHelp()
	{
		System.out.println("-HELP- n");
		System.out.println(" "); 
		System.out.println("-url=<quiz URL>");
	}
	private static String getUrlSource(String url) throws IOException 
	{
		URL quizURL = new URL(url);
		URLConnection connection = quizURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		{
			a.append(inputLine);
		}
		in.close();
		return a.toString().trim(); 
	}
	
	public static void printAnswerPairs(String pageSource)
	{
		String[] split_code = splitCode(pageSource);
		System.out.println("~Print Extracted Array Data~"); 
		for (int i = 0; i < split_code.length; i++)
		{
			System.out.println(split_code[i]); 
		}
		firstParse(split_code[0], split_code[1]); 
	}
	public static void firstParse(String arrayIndex, String arrayValue)
	{
		String ansIndexRaw = arrayIndex;
		String sRaw = arrayValue;
		//System.out.println(ansIndexRaw); 
		int counter = 1; 
		System.out.println("~Print Ans~"); 
		if (ansIndexRaw.startsWith("multi:"))
		{
			
			ansIndexRaw = ansIndexRaw.replace("multi:",  "");
			String[] ansIndexSplit = ansIndexRaw.split("`");
			String[] sSplit = sRaw.split("`");
			for (int i = 0; i < ansIndexSplit.length; i++)
			{
				int ansIndex = Integer.parseInt(ansIndexSplit[i]);
				int value = (ansIndex %31) + 1;
				String s = sSplit[i];
				//addListdecodeValue(ansIndex, value, s); 
				System.out.println("-" + counter + ")" + decodeValue(ansIndex, value, s)); 
				counter++; 
			}
		}
		
		else
		{
			int ansIndex = Integer.parseInt(ansIndexRaw);
			int value = (ansIndex %31) + 1;
			String s = sRaw;
			//addListdecodeValue(ansIndex, value, s); 
			System.out.println("-" + counter + ")" + decodeValue(ansIndex, value, s));  
		}
		
	}
	public static String[] splitCode(String page_code)
	{
		String ansIndexArray = "";
		String sArray = "";
		String[] splitSource = page_code.split("ansMap\\[");
		System.out.println("~Print Src~"); 
		System.out.println(page_code); 
		for (int i = 0; i < splitSource.length; i++)
		{
			if (i != 0 && i != splitSource.length && i != splitSource.length - 1 && i != splitSource.length -2)
			{
				String rawSplit = splitSource[i];
				String[] firstSplit = rawSplit.split("';");
				String first = firstSplit[0];
				String[] secondSplit = first.split("\\] = '");
				String ansIndex = secondSplit[0];
				String s = secondSplit[1];
				ansIndexArray += ansIndex + "`";
				sArray += s + "`";
			}
		}
		ansIndexArray = "multi:" + ansIndexArray.substring(0, ansIndexArray.length() - 1);
		sArray = sArray.substring(0, sArray.length() - 1);
		String arrayIndex = ansIndexArray;
		String arrayValue = sArray;
		String[] returnValue = new String[2];
		returnValue[0] = arrayIndex;
		returnValue[1] = arrayValue;
		return returnValue; 
	}
	
	public static String fromCharCode(int... codePoints) {
	    StringBuilder builder = new StringBuilder(codePoints.length);
	    for (int codePoint : codePoints) {
	        builder.append(Character.toChars(codePoint));
	    }
	    return builder.toString();
	}
	public static String decodeValue(int ansIndex, int value, String s)
	{
		String newString = "";
		int code = 0;
		for (int i = 0; i < s.length(); i += 2)
		{
			code = Integer.parseInt(s.substring(i, i+2), 16);
			newString += fromCharCode(code^value);
		}
		return newString; 
	}
}
