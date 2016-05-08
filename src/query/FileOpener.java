package query;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import cse131.ArgsProcessor;
/*
 * 
 * @author: Ron Cytron 
 */
public class FileOpener{

	private final File f;
	private final BufferedReader br;
	private String line;

	public FileOpener(File f) throws IOException{
		this.f = f;
		InputStream fis = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		br = new BufferedReader(isr);
		line = br.readLine();
	}

	public FileOpener() throws IOException {
		this(ArgsProcessor.chooseFile("/Documents"));

	}

	public String toString() {
		return "" + f;
	}

	public Iterator<String> iter() {
		return new Iterator<String>() {

			@Override
			public boolean hasNext() {
				return line != null;
			}

			@Override
			public String next() {
				String ans = line;
				try {
					line = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ans;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
			}

		};
	}

	public static void main(String[] args) throws IOException {
		FileOpener f = new FileOpener();
		System.out.println(f);
		Iterator<String> i = f.iter();
		while (i.hasNext()) {
			System.out.println("Line: " + i.next());
		}
	}
}
