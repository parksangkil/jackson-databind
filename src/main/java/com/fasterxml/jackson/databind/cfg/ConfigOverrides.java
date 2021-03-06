package com.fasterxml.jackson.databind.cfg;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Container for individual {@link ConfigOverride} values.
 * 
 * @since 2.8
 */
public class ConfigOverrides
    implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Per-type override definitions
     */
    protected Map<Class<?>, MutableConfigOverride> _overrides;

    // // // Global defaulting

    /**
     * @since 2.9
     */
    protected JsonInclude.Value _defaultInclusion;

    /**
     * @since 2.9
     */
    protected JsonSetter.Value _defaultSetterInfo;

    /*
    /**********************************************************
    /* Life cycle
    /**********************************************************
     */

    public ConfigOverrides() {
        this(null,
                // !!! TODO: change to (ALWAYS, ALWAYS)?
                JsonInclude.Value.empty(),
                JsonSetter.Value.empty()
        );
    }

    protected ConfigOverrides(Map<Class<?>, MutableConfigOverride> overrides,
            JsonInclude.Value defIncl,
            JsonSetter.Value defSetter) {
        _overrides = overrides;
        _defaultInclusion = defIncl;
        _defaultSetterInfo = defSetter;
    }

    public ConfigOverrides copy()
    {
        if (_overrides == null) {
            return new ConfigOverrides();
        }
        Map<Class<?>, MutableConfigOverride> newOverrides = _newMap();
        for (Map.Entry<Class<?>, MutableConfigOverride> entry : _overrides.entrySet()) {
            newOverrides.put(entry.getKey(), entry.getValue().copy());
        }
        return new ConfigOverrides(newOverrides,
                _defaultInclusion, _defaultSetterInfo);
    }

    /*
    /**********************************************************
    /* Per-type override access
    /**********************************************************
     */
    
    public ConfigOverride findOverride(Class<?> type) {
        if (_overrides == null) {
            return null;
        }
        return _overrides.get(type);
    }

    public MutableConfigOverride findOrCreateOverride(Class<?> type) {
        if (_overrides == null) {
            _overrides = _newMap();
        }
        MutableConfigOverride override = _overrides.get(type);
        if (override == null) {
            override = new MutableConfigOverride();
            _overrides.put(type, override);
        }
        return override;
    }

    /*
    /**********************************************************
    /* Global defaults access
    /**********************************************************
     */

    public JsonInclude.Value getDefaultInclusion() {
        return _defaultInclusion;
    }

    public JsonSetter.Value getDefaultSetterInfo() {
        return _defaultSetterInfo;
    }

    public void setDefaultInclusion(JsonInclude.Value v) {
        _defaultInclusion = v;
    }

    public void setDefaultSetterInfo(JsonSetter.Value v) {
        _defaultSetterInfo = v;
    }

    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */
    
    protected Map<Class<?>, MutableConfigOverride> _newMap() {
        return new HashMap<Class<?>, MutableConfigOverride>();
    }
}
