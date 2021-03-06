package fk;

@SuppressWarnings("serial")
public class OAuth_Servlet extends javax.servlet.http.HttpServlet 
{
  public void Do_Get_Post(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) 
  throws java.io.IOException 
  {
    String hdr_auth, fwd_content_type, method, uri, qry;
    java.net.URL fwdURL;
    java.net.HttpURLConnection fwdConnection;
    int fwd_res_code;
    
    // extract relevant details from incoming browser Xero call
    hdr_auth = req.getHeader("Authorization");
    method = req.getMethod();
    qry = req.getQueryString();
    uri = req.getRequestURI();
    if (qry!=null)
      uri=uri+"?"+qry;
    
    // call xero
    fwdURL = new java.net.URL("https://api.xero.com"+uri);
    fwdConnection = (java.net.HttpURLConnection)fwdURL.openConnection();
    fwdConnection.setRequestProperty ("Authorization", hdr_auth);
    fwdConnection.setRequestProperty ("Accept", "application/json");
    fwdConnection.setRequestMethod(method);
    fwdConnection.setReadTimeout(0);
    
    fwd_res_code = fwdConnection.getResponseCode();
    fwd_content_type = fwdConnection.getContentType();
    
    Set_Headers(res);
    res.setContentType(fwd_content_type);
    res.setStatus(fwd_res_code);
    Set_Content(res, fwdConnection);
  }
  
  public void Set_Content(javax.servlet.http.HttpServletResponse res, java.net.HttpURLConnection fwdConnection)
  throws java.io.IOException 
  {
    int len = 0;
    byte[] buf = new byte[1024];
    java.io.InputStream in;
    java.io.OutputStream out;
    
    in = fwdConnection.getInputStream();
    out = res.getOutputStream();
    while ((len = in.read(buf)) > 0) 
    {
      out.write(buf, 0, len);
    }
    in.close();
  }
  
  public void Set_Headers(javax.servlet.http.HttpServletResponse res)
  {
    res.addHeader("Access-Control-Allow-Origin", "*");
    res.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    res.addHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");    
  }
  
  public void doOptions(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res)
  throws java.io.IOException 
  { 
    Set_Headers(res);
    res.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
  }
  
  public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) 
  throws java.io.IOException 
  {
    Do_Get_Post(req, res);
  }
  
  public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res)
  throws java.io.IOException 
  {
    Do_Get_Post(req, res);
  }
}