package cz.chovanecm.snow.datalayer.rest.impl.activerecord;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import lombok.Getter;

@Getter
public class BusinessRuleActiveRecord implements ActiveRecord {
    private BusinessRuleSnowScript businessRule;
    private SnowRestInterface restInterface;

    public BusinessRuleActiveRecord(BusinessRuleSnowScript businessRule, SnowRestInterface restInterface) {
        this.businessRule = businessRule;
        this.restInterface = restInterface;
    }

    @Override
    public void save() {
        getRestInterface().saveRecord("sys_script",
                getBusinessRule().getSysId(),
                asJsonObject());
    }

    private JsonObject asJsonObject() {
        JsonObject json = new JsonObject();
        json.put("script", getBusinessRule().getScript());
        return json;
    }
}
