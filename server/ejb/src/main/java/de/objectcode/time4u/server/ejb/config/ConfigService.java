package de.objectcode.time4u.server.ejb.config;

import java.net.InetAddress;
import java.util.Date;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;

import de.objectcode.time4u.server.entities.ClientEntity;

@Service
@Management(IConfigServiceManagement.class)
@Local(IConfigServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/ConfigService/local")
public class ConfigService implements IConfigServiceManagement, IConfigServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  private long m_serverId;

  public long getServerId()
  {
    return m_serverId;
  }

  public void start() throws Exception
  {
    final Query query = m_manager.createQuery("from " + ClientEntity.class.getName() + " c where c.myself = :myself");

    query.setParameter("myself", true);

    try {
      final ClientEntity clientEntity = (ClientEntity) query.getSingleResult();

      if (clientEntity != null) {
        m_serverId = clientEntity.getClientId();
        return;
      }
    } catch (final NoResultException e) {
    }

    final byte[] address = InetAddress.getLocalHost().getAddress();
    m_serverId = ((long) address[0] & 0xff) << 56 | ((long) address[1] & 0xff) << 48 | ((long) address[2] & 0xff) << 40
        | ((long) address[3] & 0xff) << 32;

    final ClientEntity clientEntity = new ClientEntity();
    clientEntity.setClientId(m_serverId);
    clientEntity.setMyself(true);
    clientEntity.setServer(true);
    clientEntity.setRegisteredAt(new Date());

    m_manager.persist(clientEntity);
  }

  public void stop()
  {
  }

}
