package de.objectcode.time4u.server.entities.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.type.NullableType;
import org.hibernate.type.TypeFactory;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

/**
 * Mostly copied from <a href="http://www.hibernate.org/272.html">http://www.hibernate.org/272.html</a>.
 */
public class GenericEnumUserType implements EnhancedUserType, ParameterizedType
{
  private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "name";
  private static final String DEFAULT_VALUE_OF_METHOD_NAME = "valueOf";

  @SuppressWarnings("unchecked")
  private Class<? extends Enum> enumClass;
  private Class<?> identifierType;
  private Method identifierMethod;
  private Method valueOfMethod;
  private NullableType type;
  private int[] sqlTypes;

  public void setParameterValues(final Properties parameters)
  {
    final String enumClassName = parameters.getProperty("enumClass");
    try {
      enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
    } catch (final ClassNotFoundException cfne) {
      throw new HibernateException("Enum class not found", cfne);
    }

    final String identifierMethodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);

    try {
      identifierMethod = enumClass.getMethod(identifierMethodName, new Class[0]);
      identifierType = identifierMethod.getReturnType();
    } catch (final Exception e) {
      throw new HibernateException("Failed to obtain identifier method", e);
    }

    type = (NullableType) TypeFactory.basic(identifierType.getName());

    if (type == null) {
      throw new HibernateException("Unsupported identifier type " + identifierType.getName());
    }

    sqlTypes = new int[] { type.sqlType() };

    final String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);

    try {
      valueOfMethod = enumClass.getMethod(valueOfMethodName, new Class[] { identifierType });
    } catch (final Exception e) {
      throw new HibernateException("Failed to obtain valueOf method", e);
    }
  }

  public Class<?> returnedClass()
  {
    return enumClass;
  }

  public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
      SQLException
  {
    final Object identifier = type.get(rs, names[0]);
    if (rs.wasNull()) {
      return null;
    }

    try {
      return valueOfMethod.invoke(enumClass, new Object[] { identifier });
    } catch (final Exception e) {
      throw new HibernateException("Exception while invoking valueOf method '" + valueOfMethod.getName() + "' of "
          + "enumeration class '" + enumClass + "'", e);
    }
  }

  public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
      SQLException
  {
    try {
      if (value == null) {
        st.setNull(index, type.sqlType());
      } else {
        final Object identifier = identifierMethod.invoke(value, new Object[0]);
        type.set(st, identifier, index);
      }
    } catch (final Exception e) {
      throw new HibernateException("Exception while invoking identifierMethod '" + identifierMethod.getName() + "' of "
          + "enumeration class '" + enumClass + "'", e);
    }
  }

  public int[] sqlTypes()
  {
    return sqlTypes;
  }

  public Object assemble(final Serializable cached, final Object owner) throws HibernateException
  {
    return cached;
  }

  public Object deepCopy(final Object value) throws HibernateException
  {
    return value;
  }

  public Serializable disassemble(final Object value) throws HibernateException
  {
    return (Serializable) value;
  }

  public boolean equals(final Object x, final Object y) throws HibernateException
  {
    return x == y;
  }

  public int hashCode(final Object x) throws HibernateException
  {
    return x.hashCode();
  }

  public boolean isMutable()
  {
    return false;
  }

  public Object replace(final Object original, final Object target, final Object owner) throws HibernateException
  {
    return original;
  }

  @SuppressWarnings("unchecked")
  public Object fromXMLString(final String xmlValue)
  {
    return Enum.valueOf(enumClass, xmlValue);
  }

  public String objectToSQLString(final Object value)
  {
    try {
      final Object identifier = identifierMethod.invoke(value, new Object[0]);
      return '\'' + identifier.toString() + '\'';
    } catch (final Exception e) {
      throw new HibernateException("Exception while invoking identifierMethod '" + identifierMethod.getName() + "' of "
          + "enumeration class '" + enumClass + "'", e);
    }
  }

  public String toXMLString(final Object value)
  {
    return ((Enum<?>) value).name();
  }

}
