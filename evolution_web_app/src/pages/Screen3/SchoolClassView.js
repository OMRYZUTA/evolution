import * as React from 'react';
import {useCallback, useEffect, useState} from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Typography from "@mui/material/Typography";
import {makeStyles} from "@mui/styles"
import Grid from "@mui/material/Grid";
import DropDown from "../../components/Dropdown";
import TabularTimeTable from "./TabularTimeTable";


const useStyles = makeStyles(() => ({
    dropdown: {
        width: "100px"
    }
}));


const extractTeachersFromTeacherObject = (teachersObject) => {
    return Object.keys(teachersObject).map(key => teachersObject[key]);
}

const timeSlotToString = (quintets) => {
    let slotString = "";

    for (let i = 0; i < quintets.length; i++) {
        let id1 = quintets[i].schoolClassID;
        let s1 = quintets[i].schoolClassName;
        let id2 = quintets[i].subjectID;
        let s2 = quintets[i].subjectName;
        slotString += `${id1} ${s1}, ${id2} ${s2}\n`;
    }

    return slotString;
}


const SchoolClassView = ({quintets, days, hours, teachersObject}) => {
    const classes = useStyles();
    const [teachers] = useState(extractTeachersFromTeacherObject(teachersObject));
    const [currentTeacher, setCurrentTeacher] = useState(teachers[0]);
    const [teacherQuintets, setTeacherQuintets] = useState(quintets.filter(quintet => quintet.teacherID === currentTeacher.id));



    const setValueInTeacher = useCallback((name, value) => {
        setTeacherQuintets(quintets.filter(quintet => quintet.teacherID === value));
        setCurrentTeacher(teachers.find(teacher => teacher.id === value))
    }, []);

    return (
        <Grid container direction={"column"}>
            <Grid item>
                <Grid container xs={6} md={3}>
                    <DropDown
                        label={"Teacher"}
                        options={teachers.map(teacher => ({
                            id: teacher.id, name: teacher.name
                        }))}
                        currentValue={currentTeacher.id}
                        keyPropName="id"
                        namePropName="name"
                        onChange={(e) => setValueInTeacher('name', e.target.value)}
                    />
                </Grid>
            </Grid>
            <Grid item>
                <TabularTimeTable days={days} hours={hours} quintets={teacherQuintets}
                                  timeSlotToString={timeSlotToString}/>
            </Grid>
        </Grid>
    );
}

export default SchoolClassView;