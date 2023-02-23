package ${enumPackage};


public enum ${entity}Field {
<#list table.fields as field>
        <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime"&&"${field.propertyName}"!="remark" &&"${field.propertyName}"!="deleteFlag" && (field_has_next && "${field.propertyName}"!="tenantId")>
                ${field.name?upper_case}("${field.name}", "${field.comment}"),
        </#if></#list>;
        private final String code;
        private final String info;
        ${entity}Field(String code, String info) {
        this.code = code;
        this.info = info;
        }

        public String getCode() {
        return code;
        }

        public String getInfo() {
        return info;
        }
}

