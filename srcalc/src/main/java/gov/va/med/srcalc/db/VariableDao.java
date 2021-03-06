package gov.va.med.srcalc.db;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.common.collect.*;

import gov.va.med.srcalc.domain.model.*;

/**
 * DAO for {@link AbstractVariable}s.
 */
@Repository
public class VariableDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VariableDao.class);
    
    private final SessionFactory fSessionFactory;
    
    /**
     * Constructs an instance.
     * @param sessionFactory the SessionFactory used to get the current session.
     */
    @Inject // Allow arguments to be autowired.
    public VariableDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    /**
     * @see org.hibernate.SessionFactory#getCurrentSession()
     */
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }

    /**
     * <p>Returns all of the Variables defined in the database, sorted by display
     * name (case insensitive).</p>
     */
    @SuppressWarnings("unchecked") // trust Hibernate
    public List<AbstractVariable> getAllVariables()
    {
        // As far as I can tell, HQL "order by" simply translates to a SQL
        // "ORDER BY", thus making the case-insensitivity up the database's
        // responsibility. So we do the sorting as a post-processing step in
        // Java to ensure portability.
        final Query q = getCurrentSession().createQuery(
                "from AbstractVariable v order by v.displayName");
        final List<AbstractVariable> vars = q.list();
        Collections.sort(vars, new DisplayNameComparator());
        return vars;
    }
    
    /**
     * Returns all VariableGroups in the database, in arbitrary order.
     * @return an ImmutableCollection
     */
    public ImmutableCollection<VariableGroup> getAllVariableGroups()
    {
        LOGGER.trace("Loading all VariableGroups.");

        @SuppressWarnings("unchecked") // trust Hibernate
        final List<VariableGroup> list =
                getCurrentSession().createCriteria(VariableGroup.class).list();
        
        return ImmutableList.copyOf(list);
    }
    
    /**
     * Returns the persistent Variable with the given key, or null
     * if no such Variable exists.
     * 
     * @return a <i>persistent</i> object: any changes made during the current
     * transaction will automatically be persisted
     */
    public AbstractVariable getByKey(final String variableKey)
    {
        final Query q =  getCurrentSession().createQuery(
                "from AbstractVariable v where v.key = :variableKey");
        q.setString("variableKey", variableKey);
        return (AbstractVariable)q.uniqueResult();
    }
    
    /**
     * <p>Persists the given variable to the database using <a
     * href="http://en.wikibooks.org/wiki/Java_Persistence/Persisting#Merge">JPA
     * merge semantics</a>.</p>
     * 
     * <p>As stated in {@link #getByKey(String) getByKey}, changes are usually
     * automatically persisted. Call this only to persist a brand-new object or
     * to persist changes made after the transaction in which the object was
     * loaded.</p>
     * 
     * <p>Note that the given object is not added to the persistence context,
     * but the returned object is. If you want to modify further state, use the
     * returned object.</p>
     * 
     * @param variable represents the state to persist
     */
    public AbstractVariable mergeVariable(final AbstractVariable variable)
    {
        LOGGER.debug("Merging {} into persistence context.", variable);
        // Trust Hibernate with this cast here. (I wish it was generic.)
        return (AbstractVariable)getCurrentSession().merge(variable);
    }
}
