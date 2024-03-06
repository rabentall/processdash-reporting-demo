package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

/**
 * \brief Provides access to the different key-value pair lookups used by the API.
 * These are not accessible via the jsonViews API, but are used to construct data returned
 * by the API.
 */
public class Lookups {

    PDashContext ctx_;

    //================================================================================

    String MILESTONE_COLOR_HQL =
    " select                                 " +
    "   piaf.planItem.id,                    " +
    "   piaf.value.text                      " +
    " from                                   " +
    "   PlanItemAttrFact as piaf             " +
    " where                                  " +
    "   piaf.attribute.name='Milestone Color'";

    public Lookup<String> milestoneColors = new Lookup<String>(MILESTONE_COLOR_HQL);

    //================================================================================

    String MILESTONE_COMMIT_DATE_HQL =
    " select                                       " +
    "   piaf.planItem.id,                          " +
    "   piaf.value.text                            " +
    " from                                         " +
    "   PlanItemAttrFact as piaf                   " +
    " where                                        " +
    "   piaf.attribute.name='Milestone Commit Date'";

    public Lookup<String> milestoneCommitDate = new Lookup<String>(MILESTONE_COMMIT_DATE_HQL);

    //================================================================================

    String TASK_NAME_HQL =
    " select       " +
    "    pi.id,     " +
    "    pi.task.name    " +
    " from         " +
    "    PlanItem as pi ";

    public Lookup<String> taskNames = new Lookup<String>(TASK_NAME_HQL);

    //================================================================================

    String PROCESS_NAME_HQL =
    " select                   " +
    "    pi.id,                " +
    "    pi.phase.process.name " +
    " from                     " +
    "    PlanItem as pi        ";

    public Lookup<String> processNames = new Lookup<String>(PROCESS_NAME_HQL);

    //================================================================================

    String PHASE_SHORTNAME_HQL =
    " select                " +
    "    pi.id,             " +
    "    pi.phase.shortName " +
    " from                  " +
    "    PlanItem as pi     ";

    public Lookup<String> phaseShortNames = new Lookup<String>(PHASE_SHORTNAME_HQL);


    //================================================================================

    String PHASE_TYPENAME_HQL =
    " select                " +
    "    pi.id,             " +
    "    pi.phase.typeName " +
    " from                  " +
    "    PlanItem as pi     ";

    public Lookup<String> phaseTypeNames = new Lookup<String>(PHASE_TYPENAME_HQL);

    //================================================================================

    transient String PHASE_NAME_HQL =
    " select          " +
    "   pi.id,        " +
    "   pi.phase.name " +
    " from            " +
    "   PlanItem as pi ";

    transient Lookup<String> phaseNames = new Lookup<String>(PHASE_NAME_HQL);

    public Lookups(PDashContext ctx){
        ctx_ = ctx;
    }

    public void load(){
        milestoneColors.load(ctx_);
        milestoneCommitDate.load(ctx_);
        taskNames.load(ctx_);
        processNames.load(ctx_);
        phaseShortNames.load(ctx_);
        phaseTypeNames.load(ctx_);
        phaseNames.load(ctx_);
    }

}
