package org.neurodb;

public enum ResultStatus {
    PARSER_OK(1, ""),             /*运行成功*/
    NO_MEM_ERR(2, "NO_MEM_ERR"),            /*内存分配异常*/
    SYNTAX_ERR(3, "SYNTAX_ERR"),           /*普通语法错误*/
    NO_Exp_ERR(4, "NO_Exp_ERR"),          /*未找到此指令*/
    NO_LNK_ERR(5, "NO_LNK_ERR"),            /*缺少关系*/
    NO_ARROW_ERR(6, "NO_ARROW_ERR"),          /*缺少箭头*/
    DOU_ARROW_ERR(7, "DOU_ARROW_ERR"),         /*关系双箭头错误*/
    NO_HEAD_ERR(8, "NO_HEAD_ERR"),          /*缺少头节点*/
    NO_TAIL_ERR(9, "NO_TAIL_ERR"),           /*缺少尾结点*/
    CHAR_NUM_UL_ERR(10, "CHAR_NUM_UL_ERR"),     /*必须是字母数字下划线*/
    NOT_PATTERN_ERR(11, "NOT_PATTERN_ERR"),      /*不是模式表达式*/
    DUP_VAR_NM_ERR(12, "DUP_VAR_NM_ERR"),      /*此变量已被使用*/
    NO_SM_TYPE_ERR(13, "NO_SM_TYPE_ERR"),      /*数组中含有不相同的类型*/
    NO_SUP_TYPE(14, "NO_SUP_TYPE"),        /*不支持的数据类型*/
    WRON_EXP(15, "WRON_EXP"),          /*指令搭配错误*/
    NOT_SUPPORT(16, "NOT_SUPPORT"),          /*暂不支持的指令*/
    WHERE_SYN_ERR(17, "WHERE_SYN_ERR"),        /*where语句语法*/
    WHERE_RUN_ERR(18, "WHERE_RUN_ERR"),        /*where运算语法*/
    NO_VAR_ERR(19, "NO_VAR_ERR"),            /*未找到变量*/
    NO_PAIR_BRK(20, "NO_PAIR_BRK"),           /*缺失配对括号*/
    CLIST_HAS_LINK_ERR(21, "CLIST_HAS_LINK_ERR"),   /*删除带有关边的节点*/
    CLIST_OPR_ERR(22, "CLIST_OPR_ERR"),       /*数据操作错误*/
    ORDER_BY_SYN_ERR(23, "ORDER_BY_SYN_ERR"),     /*order by语句语法*/
    DEL_PATH_ERR(24, "DEL_PATH_ERR"),        /*不可删除路径*/
    UNDEFINED_VAR_ERR(25, "UNDEFINED_VAR_ERR"),     /*未定义的变量*/
    WHERE_PTN_NO_VAR_ERR(26, "WHERE_PTN_NO_VAR_ERR"),  /*where 模式条件，独立连通图缺少变量*/
    NO_PROC_ERR(27, "NO_PROC_ERR"),          /*不支持的存储过程*/
    CSV_FILE_ERR(28, "CSV_FILE_ERR"),             /*csv文件读取错误*/
    CSV_ROW_VAR_ERR(29, "CSV_ROW_VAR_ERR");     /*csv 变量属性名在列中未找到*/

    private Integer type;
    private String name;

    private ResultStatus(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
