package DcmsApp;

/**
* DcmsApp/DcmsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from IDLInterfaces/Dcms.idl
* Saturday, June 16, 2018 11:55:42 o'clock AM EDT
*/

public final class DcmsHolder implements org.omg.CORBA.portable.Streamable
{
  public DcmsApp.Dcms value = null;

  public DcmsHolder ()
  {
  }

  public DcmsHolder (DcmsApp.Dcms initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = DcmsApp.DcmsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    DcmsApp.DcmsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return DcmsApp.DcmsHelper.type ();
  }

}
