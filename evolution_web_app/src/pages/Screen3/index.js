import { Grid, Container,} from '@mui/material';
import Navbar from "../../components/Navbar";
import {UserContext} from "../../components/UserContext"
import {useContext} from "react";
import {makeStyles} from "@material-ui/core/styles";

import Paper from "@mui/material/Paper";

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
    actions:{
        padding:20,
        margin:20,
        justifyContent:'space-evenly',

    }
}));
export default function Screen3() {
    const {currentUser} = useContext(UserContext);
    const classes = useStyles();
    const actions = ["start ","pause ","resume ","stop "]
    return (
        <Grid>
            <Container maxWidth="xl">
                <Navbar user={currentUser}/>
                <Grid container className={classes.root} direction={"row"}>
                    <Grid item xs={12} md={4}>
                        <Paper className={classes.settings}/>
                    </Grid>
                    <Grid item xs={12} md={8}>
                        <Grid container direction={"row"} className={classes.actions}>
                            {actions.map(action => {
                                return (
                                    <Paper>{action}</Paper>
                                )
                            })}
                        </Grid>
                    </Grid>
                </Grid>
            </Container>
        </Grid>
    );
}