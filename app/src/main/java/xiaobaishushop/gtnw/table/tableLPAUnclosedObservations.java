package xiaobaishushop.gtnw.table;

import android.widget.Button;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "未关闭的审核问题清单")
public class tableLPAUnclosedObservations {
    @SmartColumn(id = 1, name = "#" ,width = 30)
    private String id;
    @SmartColumn(id = 3, name = "Area" ,width = 50)
    private String area;
    @SmartColumn(id = 5, name = "Auditor" ,width = 25)
    private String auditor;
    @SmartColumn(id = 6, name = "Audit Time" ,width = 25)
    private String auditTime;
    @SmartColumn(id = 7, name = "Audit List",width = 50)
    private String auditList;
    @SmartColumn(id = 8, name = "Item Descibe" ,width = 150)
    private String itemDescibe;
    @SmartColumn(id = 9, name = "Category" ,width = 50)
    private String category;
    @SmartColumn(id = 10, name = "Observation" ,width = 180)
    private String observation;
    @SmartColumn(id = 11, name = "Action" ,width = 150)
    private String action;
    @SmartColumn(id = 12, name = "Owner" ,width = 50)
    private String owner;
    @SmartColumn(id = 13, name = "Due date" ,width = 50)
    private String dueDate;
    @SmartColumn(id = 14, name = "Update",width = 50)
    private Button updateBtn;

    public String getAuditList() {
        return auditList;
    }

    public void setAuditList(String auditList) {
        this.auditList = auditList;
    }

    public String getItemDescibe() {
        return itemDescibe;
    }

    public void setItemDescibe(String itemDescibe) {
        this.itemDescibe = itemDescibe;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Button getUpdateBtn() {
        return updateBtn;
    }

    public void setUpdateBtn(Button updateBtn) {
        this.updateBtn = updateBtn;
    }
}