import CircularIndeterminate from "../../components/CircularIndeterminate";
import {DataGrid} from '@mui/x-data-grid';
import Grid from "@mui/material/Grid";
import {makeStyles} from "@mui/styles";
import Paper from "@mui/material/Paper";
import React from 'react';
import Typography from "@mui/material/Typography";

const useStyles = makeStyles((theme) => ({
    root: {
        spacing: 2,
        justifyContent: "space-between"
    },
    settings: {
        spacing: 2,
        justifyContent: "space-between",
        backgroundColor: "#D3D3D3", //light gray
    },
    requirements: {
        padding: 10,
    },
    schoolClass: {
        border: 1,
        borderBlockColor: "black",
        padding: 10,
        margin: 50,
    }
}));

const OtherSolutions = ({otherSolutionsList}) => {
    const classes = useStyles();

    const columns = [
        {field: 'userName', headerName: 'User', width: 150, editable: false},
        {
            field: 'bestScore', headerName: 'Best Score', width: 90, editable: false,
        },
        {
            field: 'currentGeneration',
            headerName: 'Generation',
            width: 30,
            editable: false,
        },
    ];

    // if it's undefined
    if (!otherSolutionsList) {
        return (<CircularIndeterminate/>);
    }

    return (
        <Paper>
            <Grid container direction={"column"} className={classes.root}>
                <Typography>
                    other users are solving the same problem:
                </Typography>
                <Grid item>
                    <Grid container className={classes.settings}>
                        <DataGrid
                            rows={otherSolutionsList}
                            columns={columns}
                            pageSize={5}
                            rowsPerPageOptions={[5]}
                        />
                    </Grid>
                </Grid>
            </Grid>
        </Paper>
    );
}

export default OtherSolutions;