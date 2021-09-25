import {makeStyles} from "@material-ui/core/styles";
import {Typography} from "@mui/material";
import Typography from "@mui/material/Typography";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
        minHeight: "100vh",
    },
    settings: {
        width: '100%',
        height: 400,
        maxWidth: 300,
        backgroundColor: "#D3D3D3", //light gray
    },
}));

const TimetableDetails = ({timetable}) => {
    //days, hours subjects, teachers, classes, rules
    return (
        <Paper>
            {timetable.subjects.map(subject => <Typography>{subject.id} {subject.name}</Typography>)}


        </Paper>
    );
}

export default TimetableDetails;