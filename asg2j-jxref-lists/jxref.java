// Brian Lin bjlin
// $Id: jxref.java,v 1.8 2013-10-16 17:10:32-07 - - $

import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.System.*;

class jxref {
   private static final String STDIN_FILENAME = "-";
   private static final String REGEX = "\\w+([-'.:/]\\w+)*";
   private static final Pattern PATTERN = Pattern.compile(REGEX);

   private static void xref_file (String filename, Scanner file){
      for(int i=0;i<32;i++)out.print(":");
      out.println("\n"+filename);
      for(int i=0;i<32;i++)out.print(":");
      out.println();
      listmap map = new listmap();
      for (int linenr = 1; file.hasNextLine(); ++linenr) {
         String line = file.nextLine();
         Matcher match = PATTERN.matcher (line);
         while (match.find()) {
            String word = match.group();
            map.insert(word,linenr);
         }
      }
      for (Entry<String, intqueue> entry: map) {
         out.print(entry.getKey()+" ["+entry.getValue().getcount()+"]");
         for(Integer linenr: entry.getValue())
            out.print(" "+linenr);
         out.println();
      }
   }


   // For each filename, open the file, cross reference it,
   // and print.
   private static void xref_filename (String filename) {
      if (filename.equals (STDIN_FILENAME)) {
         xref_file (filename, new Scanner (System.in));
      }else {
         try {
            Scanner file = new Scanner (new File (filename));
            xref_file (filename, file);
            file.close();
         }catch (IOException error) {
            misc.warn (error.getMessage());
         }
      }
   }

   // Main function scans arguments to cross reference files.
   public static void main (String[] args) {
      if (args.length == 0) {
         xref_filename (STDIN_FILENAME);
      }else {
         for (String filename: args) {
            xref_filename (filename);
         }
      }
      exit (misc.exit_status);
   }

}

