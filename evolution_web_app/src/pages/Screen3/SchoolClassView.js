import * as React from 'react';
import {useCallback, useState} from 'react';
import {makeStyles} from "@mui/styles"
import Grid from "@mui/material/Grid";
import DropDown from "../../components/Dropdown";
import TabularTimeTable from "./TabularTimeTable";
import * as Utils from "../../Utils/Utils"

const useStyles = makeStyles(() => ({
    dropdown: {
        width: "100px"
    }
}));


const timeSlotToString = (quintets) => {
    let slotString = "";


    for (let i = 0; i < quintets.length; i++) {
        let id1 = quintets[i].teacherID;
        let s1 = quintets[i].teacherName;
        let id2 = quintets[i].subjectID;
        let s2 = quintets[i].subjectName;
        slotString += `${id1} ${s1}, ${id2} ${s2}\n`;
    }

    return slotString;
}


const SchoolClassView = ({quintets, days, hours, schoolClassesObject}) => {
    useStyles();
    const [schoolClasses] = useState(Utils.convertObjectToArray(schoolClassesObject));
    const [currentSchoolClass, setCurrentSchoolClass] = useState(schoolClasses[0]);
    const [schoolClassesQuintets, setTeacherQuintupletsClasses] = useState(quintets.filter(quintet => quintet.schoolClassID === currentSchoolClass.id));



    const setValueInSchoolClass = useCallback((name, value) => {
        setTeacherQuintupletsClasses(quintets.filter(quintet => quintet.schoolClassID === value));
        setCurrentSchoolClass(schoolClasses.find(schoolClass => schoolClass.id === value))
    }, []);

    return (
        <Grid container direction={"column"}>
            <Grid item>
                <Grid container xs={6} md={3}>
                    <DropDown
                        label={"school Class"}
                        options={schoolClasses.map(schoolClass => ({
                            id: schoolClass.id, name: schoolClass.name
                        }))}
                        currentValue={currentSchoolClass.id}
                        keyPropName="id"
                        namePropName="name"
                        onChange={(e) => setValueInSchoolClass('name', e.target.value)}
                    />
                </Grid>
            </Grid>
            <Grid item>
                <TabularTimeTable days={days} hours={hours} quintets={schoolClassesQuintets}
                                  timeSlotToString={timeSlotToString}/>
            </Grid>
        </Grid>
    );
}

export default SchoolClassView;