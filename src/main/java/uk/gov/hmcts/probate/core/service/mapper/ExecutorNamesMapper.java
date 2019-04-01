package uk.gov.hmcts.probate.core.service.mapper;

import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

public class ExecutorNamesMapper {

    public static String getFullname(Executor executor) {
        if (executor == null) {
            return null; //NOSONAR
        }
        if (executor.getFullName() !=null && !executor.getFullName().isEmpty()) {
            return executor.getFullName();
        } else if (executor.getFirstName() != null && executor.getLastName() != null) {
            return executor.getFirstName() + " " + executor.getLastName();
        }
        return null;
    }

    public static String getFirstName(String fullName){
        if(fullName==null){
            return null;
        }
        return fullName.split(" (?!.* )")[0];
    }

    public static String getLastName(String fullName){
        if(fullName==null){
            return null;
        }
        return  fullName.split(" (?!.* )")[1];
    }


}
