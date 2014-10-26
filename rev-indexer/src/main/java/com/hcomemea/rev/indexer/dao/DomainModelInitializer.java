package com.hcomemea.rev.indexer.dao;

import com.hcomemea.common.component.InitializableComponent;
import com.hcomemea.common.data.AbstractBusinessObject;
import com.hcomemea.common.registry.EntityRegistry;


/**
 * DomainModelInitializer.
 *
 * The dependencies of the domain model are not trivial and require an exact sequence of initialization,
 * this class ensures that the sequence is always the same, because in spring it is hard to
 * maintain control on initialization order.
 */
public class DomainModelInitializer implements InitializableComponent {
    private EntityRegistry entityRegistry;

    @Override
    public void initializeComponent() {
        AbstractBusinessObject.setEntityRegistry(entityRegistry);
    }

    public void setEntityRegistry(EntityRegistry entityRegistry) {
        this.entityRegistry = entityRegistry;
    }

}
