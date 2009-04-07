package de.objectcode.time4u.server.ejb.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.annotation.ejb.Depends;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.io.XMLIO;
import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.entities.ClientEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;
import de.objectcode.time4u.server.entities.report.ReportDefinitionEntity;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

@Service(objectName = "time4u:service=ConfigService")
@Management(IConfigServiceManagement.class)
@Local(ILocalIdGenerator.class)
@LocalBinding(jndiBinding = "time4u-server/ConfigService/local")
@Depends("time4u:service=LocalIdService")
public class ConfigService implements IConfigServiceManagement, ILocalIdGenerator
{
  private final static Log LOG = LogFactory.getLog(ConfigService.class);

  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdService m_localIdCreator;

  private long m_serverId = 0L;

  Map<EntityType, EntityTypeIdGenerator> m_generators;

  public long getServerId()
  {
    return m_serverId;
  }

  public long getClientId()
  {
    return m_serverId;
  }

  public String generateLocalId(final EntityType entityType)
  {
    return m_generators.get(entityType).generateLocalId();
  }

  private static String digits(final long val, final int digits)
  {
    final long hi = 1L << digits * 4;
    return Long.toHexString(hi | val & hi - 1).substring(1);
  }

  public void start() throws Exception
  {
    final Query query = m_manager.createQuery("from " + ClientEntity.class.getName() + " c where c.myself = :myself");

    query.setParameter("myself", true);

    try {
      final ClientEntity clientEntity = (ClientEntity) query.getSingleResult();

      if (clientEntity != null) {
        m_serverId = clientEntity.getClientId();
      }
    } catch (final NoResultException e) {
    }

    if (m_serverId == 0L) {
      final byte[] address = InetAddress.getLocalHost().getAddress();
      m_serverId = ((long) address[0] & 0xff) << 56 | ((long) address[1] & 0xff) << 48
          | ((long) address[2] & 0xff) << 40 | ((long) address[3] & 0xff) << 32;

      final ClientEntity clientEntity = new ClientEntity();
      clientEntity.setClientId(m_serverId);
      clientEntity.setMyself(true);
      clientEntity.setServer(true);
      clientEntity.setRegisteredAt(new Date());

      m_manager.persist(clientEntity);
    }
    final Map<EntityType, EntityTypeIdGenerator> generators = new HashMap<EntityType, EntityTypeIdGenerator>();

    for (final EntityType type : EntityType.values()) {
      generators.put(type, new EntityTypeIdGenerator(type));
    }
    m_generators = Collections.unmodifiableMap(generators);

    if (m_manager.find(UserAccountEntity.class, "admin") == null) {
      initializeAdmin();
    }

    final Query reportCountQuery = m_manager.createQuery("select count(*) from "
        + ReportDefinitionEntity.class.getName() + " r");

    if ((Long) reportCountQuery.getSingleResult() == 0L) {
      initializeReports();
    }
  }

  public void stop()
  {
  }

  /**
   * Initialize the admin user in the database.
   */
  private void initializeAdmin()
  {
    final IPasswordEncoder encoder = new DefaultPasswordEncoder();

    final long serverId = getServerId();
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(EntityType.PERSON, null);
    final String personId = generateLocalId(EntityType.PERSON);
    final PersonEntity person = new PersonEntity(personId, revisionLock.getLatestRevision(), serverId);
    person.setSurname("admin");

    m_manager.persist(person);

    final UserAccountEntity userAccount = new UserAccountEntity("admin", encoder.encrypt("admin".toCharArray()), person);

    m_manager.persist(userAccount);

    UserRoleEntity userRole = m_manager.find(UserRoleEntity.class, "user");
    if (userRole == null) {
      userRole = new UserRoleEntity("user", "Time4U User");

      m_manager.persist(userRole);
    }
    userAccount.getRoles().add(userRole);

    UserRoleEntity adminRole = m_manager.find(UserRoleEntity.class, "admin");
    if (adminRole == null) {
      adminRole = new UserRoleEntity("admin", "Time4U Administrator");

      m_manager.persist(adminRole);
    }
    userAccount.getRoles().add(adminRole);
  }

  /**
   * Initialize a set of default report definitions.
   */
  private void initializeReports()
  {
    try {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
          "report-definition.index"), "UTF-8"));
      final List<String> reportDefinitionResources = new ArrayList<String>();
      String line;

      while ((line = reader.readLine()) != null) {
        if (line.trim().length() > 0) {
          reportDefinitionResources.add(line.trim());
        }
      }
      reader.close();

      for (final String reportDefinitionResource : reportDefinitionResources) {
        final BaseReportDefinition reportDefinition = XMLIO.INSTANCE.read(getClass().getResourceAsStream(
            reportDefinitionResource));
        final StringWriter writer = new StringWriter();
        final Reader origReader = new InputStreamReader(getClass().getResourceAsStream(reportDefinitionResource),
            "UTF-8");
        final char[] buffer = new char[8192];
        int readed;
        while ((readed = origReader.read(buffer)) > 0) {
          writer.write(buffer, 0, readed);
        }
        reader.close();
        writer.close();

        final ReportDefinitionEntity entry = new ReportDefinitionEntity(reportDefinition.getName(), reportDefinition
            .getName(), reportDefinition.getEntityType().toString(), reportDefinition.getDescription(), writer
            .toString());

        m_manager.persist(entry);
      }
    } catch (final IOException e) {
      LOG.error("Initialization error", e);
    }
  }

  private class EntityTypeIdGenerator
  {
    EntityType m_entityType;
    long m_nextLocalId;
    LocalIdEntity m_current;

    EntityTypeIdGenerator(final EntityType entityType)
    {
      m_entityType = entityType;
    }

    public synchronized String generateLocalId()
    {
      if (m_current == null || m_nextLocalId > m_current.getHiId()) {
        final LocalIdEntity localIdEntity = m_localIdCreator.getNextChunk(m_entityType);

        m_current = localIdEntity;
        m_nextLocalId = localIdEntity.getLoId();
      }
      final long localId = m_nextLocalId++;
      final StringBuffer buffer = new StringBuffer();

      buffer.append(digits(m_serverId >> 32, 8));
      buffer.append(digits(m_serverId, 8));
      buffer.append('-');
      buffer.append(digits(m_entityType.getCode(), 2));
      buffer.append('-');
      buffer.append(digits(localId, 14));

      return buffer.toString();
    }
  }
}
