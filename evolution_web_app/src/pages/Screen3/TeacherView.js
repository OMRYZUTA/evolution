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


const useStyles = makeStyles((theme) => ({
    table: {
        tableLayout: "fixed",
        width: "100%",
    },
    typography: {
        padding: "5px 10px",
    },
}));

const convertNumToDay = (day) => {
    let dayName;
    switch (day) {
        case 0:
            dayName = 'Sunday';
            break;
        case 1:
            dayName = 'Monday';
            break;
        case 2:
            dayName = 'Tuesday';
            break;
        case 3:
            dayName = 'Wednesday';
            break;
        case 4:
            dayName = 'Thursday';
            break;
        case 5:
            dayName = 'Friday';
            break;
        case 6:
            dayName = 'Saturday';
            break;
        default:
            dayName = 'Invalid day';
    }
    return dayName
}

const extractTeachersFromTeacherObject = (teachersObject) => {
    const teachers = Object.keys(teachersObject).map(key => teachersObject[key]);

    return teachers;
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


const generateMatrix = (quintets, days, hours) => {
    let matrix = [...new Array(hours)].map(elem => new Array(days));

    for (let h = 0; h < hours; h++) {
        for (let d = 0; d < days; d++) {
            matrix[h][d] = [];
        }
    }

    for (let i = 0; i < quintets.length; i++) {
        matrix[quintets[i].hour][quintets[i].day].push(quintets[i]);
    }

    let displayMatrix = [...new Array(hours)].map(elem => new Array(days));
    for (let h = 0; h < hours; h++) {
        for (let d = 0; d < days; d++) {
            displayMatrix[h][d] = timeSlotToString(matrix[h][d]);
        }
    }

    return displayMatrix;
}

const TeacherView = ({quintets, days, hours, teachersObject}) => {
    const classes = useStyles();
    const [teachers, setTeachers] = useState(extractTeachersFromTeacherObject(teachersObject));
    const [currentTeacher, setCurrentTeacher] = useState(teachers[0]);
    const [teacherQuintets, setTeacherQuintets] = useState(quintets.filter(quintet => quintet.teacherID === currentTeacher.id));
    const [rows, setRows] = useState(generateMatrix(quintets, days, hours));

    useEffect(() => {

        setRows(generateMatrix(teacherQuintets, days, hours))
    }, [currentTeacher])


    const setValueInTeacher = useCallback((name, value) => {
        setTeacherQuintets(quintets.filter(quintet => quintet.teacherID === value));
        setCurrentTeacher(teachers.find(teacher => teacher.id === value))
    }, []);

    return (
        <Grid container direction={"column"}>
            <Grid item>
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
            <Grid item>
                <TableContainer component={Paper}>
                    <Table sx={{minWidth: 650}} aria-label="simple table" className={classes.table}>
                        <TableHead>
                            <TableRow>
                                <TableCell>Hour</TableCell>
                                {Array.from(Array(days).keys()).map(day => <TableCell><Typography
                                    className={classes.typography}>{convertNumToDay(day)}</Typography></TableCell>)}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row, index) => {
                                return (
                                    <TableRow
                                        sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                                        <TableCell component="th" scope="row">
                                            {index}
                                        </TableCell>
                                        {row.map((day) => <TableCell>{day.split("\n").map(lesson => <Typography
                                            className={classes.typography}>{lesson}</Typography>)}</TableCell>)}
                                    </TableRow>
                                )
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Grid>
        </Grid>
    );
}

export default TeacherView;