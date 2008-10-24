package de.objectcode.time4u.server.web.rest.secure;

import javax.ws.rs.Path;

@Path("/secure")
public class Secure
{
  @Path("/revisions")
  public Revisions getRevisions()
  {
    return new Revisions();
  }

  @Path("/projects")
  public Projects getProjects()
  {
    return new Projects();
  }

  @Path("/tasks")
  public Tasks getTasks()
  {
    return new Tasks();
  }

  @Path("/dayinfos")
  public DayInfos getDayInfos()
  {
    return new DayInfos();
  }
}
