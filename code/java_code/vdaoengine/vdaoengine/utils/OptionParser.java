package vdaoengine.utils;

/* $Id: OptionParser.java 9012 2010-11-03 16:45:45Z spook $
 *
 *
 * Stuart Pook, Copyright 2011 Institut Curie
 */

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class OptionParser
{
	final private String[] args;
	static private class OptionDescription
	{
		final String help_text;
		final boolean required;
		boolean found;
		final boolean is_boolean;
		final String type;
		OptionDescription(String help_text, boolean required, boolean is_boolean, String type)
		{
			this.help_text = help_text;
			this.required = required;
			this.is_boolean = is_boolean;
			this.type = type;
		}
		void setFound()
		{
			found = true;
		}
		boolean missing()
		{
			return required && !found;
		}
		boolean isRequired()
		{
			return required;
		}
		boolean isBoolean()
		{
			return is_boolean;
		}
		String helpText() { return help_text + "[" + type + "]"; }
	}
	final private Map<String, OptionDescription> options = new HashMap<String, OptionDescription>();
	private int pos;
	final private String arguments;
	final private String myname;
	public OptionParser(String myname, String[] a, String arguments) {
		args = a;
		this.myname = myname;
		pos = 0;
		this.arguments = arguments;
	}
	static private String caller() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		final String canonicalName = OptionParser.class.getCanonicalName();
		int level = 0;
		while (level < stackTrace.length && !stackTrace[level].getClassName().equals(canonicalName))
			level++;
		while (level < stackTrace.length && stackTrace[level].getClassName().equals(canonicalName))
			level++;
		if (level >= stackTrace.length)
			level = stackTrace.length - 1;
		String r = stackTrace[level].getClassName();
		final int p = r.lastIndexOf('.');
		if (p != -1 && p != r.length())
			return r.substring(p + 1);
		return r;
	}
	public OptionParser(String[] a, String arguments) {
		this(caller(), a, arguments);
	}
	String myname() {
		return myname;
	}
	public Boolean booleanOption(String option, String comment) {
		options.put(option, new OptionDescription(comment, false, true, "boolean"));
		if (pos >= args.length)
			return null;
		if (optionMatch(args[pos], option)) {
			pos++;
			return Boolean.TRUE;
		}
		if (optionMatch(args[pos], "no" + option)) {
			pos++;
			return Boolean.FALSE;
		}
		return null;
	}

	Integer integerOption(String option, String what)
	{
		return integerOption(option, what, false);
	}

	Integer integerOption(String option, String what, boolean required)
	{
		OptionDescription o = options.get(option);
		if (o == null)
			options.put(option, o = new OptionDescription(what, required, false, "integer"));
		if (pos + 1 >= args.length)
			return null;
		if (!optionMatch(args[pos], option))
			return null;
		o.setFound();
		pos += 2;
		String s = args[pos - 1];
		int r = Integer.parseInt(s);
		return new Integer(r);
	}

	Double doubleOption(String option, String what)
	{
		return doubleOption(option, what, false);
	}

	Double doubleOption(String option, String what, boolean required)
	{
		OptionDescription o = options.get(option);
		if (o == null)
			options.put(option, o = new OptionDescription(what, required, false, "double"));
		if (pos + 1 >= args.length)
			return null;
		if (!optionMatch(args[pos], option))
			return null;
		o.setFound();
		pos += 2;
		String s = args[pos - 1];
		double r = Double.parseDouble(s);
		return new Double(r);
	}

	public File fileRequiredOption(String option, String what) {
		String s = stringRequiredOption(option, what);
		return s == null ? null : new File(s);
	}

	public File fileOption(String option, String what) {
		String s = stringOption(option, what, false);
		return s == null ? null : new File(s);
	}

	public String stringRequiredOption(String option, String what) {
		return stringOption(option, what, true);
	}

	public String stringOption(String option, String what) {
		return stringOption(option, what, false);
	}

	String stringOption(String option, String what, boolean required) {
		OptionDescription o = options.get(option);
		if (o == null)
			options.put(option, o = new OptionDescription(what, required, false, "String"));
		if (pos + 1 >= args.length)
			return null;
		if (!optionMatch(args[pos], option))
			return null;
		o.setFound();
		pos += 2;
		return args[pos - 1];
	}

	static private boolean optionMatch(String argument, String option)
	{
		if (!argument.endsWith(option) || !argument.startsWith("-"))
			return false;
		if (argument.length() + 1 == option.length())
			return true;
		return argument.startsWith("--") && argument.length() - 2 == option.length();
	}
	void usage()
	{
		usage(null);
	}
	void usage(String message)
	{
		if (message != null)
			System.err.println(myname + ": " + message);
		System.err.print("usage: " + myname);
		for (Map.Entry<String, OptionDescription> e : options.entrySet())
		{
			System.err.print(" ");
			boolean optional = e.getValue() == null || !e.getValue().isRequired();
			if (optional)
				System.err.print("[");
			System.err.print("--" +  e.getKey());
			if (e.getValue() != null)
				System.err.print((e.getValue().isBoolean() ? "=" : " ") + "<" + e.getValue().helpText() + ">");
			if (optional)
				System.err.print("]");
		}
		if (arguments != null)
			System.err.print(" " +  arguments);
		System.err.println("");
		System.exit(1);
	}
	public String[] done()
	{
		if (pos == args.length)
		{
			if (arguments != null)
			{
				System.err.println(myname + ": missing compulsory arguments " + arguments);
				usage();
			}
		}
		else if (args[pos].startsWith("-")) {
			System.err.println(myname + ": unknown option: " + args[pos]);
			usage();
		}
		for (Entry<String, OptionDescription> e : options.entrySet())
		{
			if (e.getValue() != null && e.getValue().missing())
			{
				System.err.println(myname + ": missing compulsory option " + e.getKey());
				usage();
			}
		}
		String[] r = new String[args.length - pos];
		for (int i = pos; i < args.length; i++)
			r[i - pos] = args[i];
		return r;
	}
}

