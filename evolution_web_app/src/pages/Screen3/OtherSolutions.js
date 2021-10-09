import CircularIndeterminate from "../../components/CircularIndeterminate";
import Grid from "@mui/material/Grid";
import {makeStyles} from "@mui/styles";
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

    // if it's undefined
    if (!otherSolutionsList) {
        return (<CircularIndeterminate/>);
    }

    return (
        <Grid container direction={"column"}>
            {otherSolutionsList.length ?
                <Typography>
                    Users solving the same problem (and their BEST score so far):
                </Typography>
                :
                <Typography>
                    No users are currently solving the problem
                </Typography>
            }
            <Grid item>
                <Grid container className={classes.settings}>

                    {otherSolutionsList.map(solution => {
                        return (
                            <Grid container className={classes.settings}>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    {solution.userName}
                                </Typography>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    {solution.currentGeneration}
                                </Typography>
                                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                                    {(solution.bestScore).toFixed(4)}
                                </Typography>
                            </Grid>
                        )
                    })}
                </Grid>
            </Grid>
        </Grid>
    );
}

export default OtherSolutions
