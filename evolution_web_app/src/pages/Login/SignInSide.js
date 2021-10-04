import * as React from 'react';
import {useContext, useState} from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import * as UserServices from '../../services/UserServices'
import {useHistory} from "react-router-dom";
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import {UserContext} from "../../components/UserContext";


const renderAlert = (alertText) => {
    return (
        <Alert severity="error">
            <AlertTitle>Error</AlertTitle>
            {alertText}
        </Alert>
    );
}

const USER_NAME_EMPTY = "empty name";
const USER_NAME_NOT_UNIQUE = "not unique name";
const SUCCESSFUL_LOGIN = "successful login";
const SCREEN2URL = "/server_Web_exploded/screen2";

const theme = createTheme();

export default function SignInSide() {
    const [userName, setUserName] = useState();
    const [alertText, setAlertText] = React.useState('');
    const {currentUser, setCurrentUser} =useContext(UserContext);

    const history = useHistory();

    const routeChange = () =>{
        let path = SCREEN2URL;
        history.push(path);
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const result = await UserServices.login(userName);
        switch (result){
            case SUCCESSFUL_LOGIN:
                initUserAndGoToScreen2();
                break;
            case USER_NAME_EMPTY:
                setAlertText('User name cannot be empty, please type at least one character');
                break;
            case USER_NAME_NOT_UNIQUE:
                setAlertText('User name already exists, please enter a different name');
                break;
            default:
                setAlertText('Unexpected Error, please contact support');
                break;
        }
    };
    const initUserAndGoToScreen2=()=>{
        document.cookie= `username=${userName};path=/`
        setCurrentUser(userName)
        routeChange();
    }

    const handleUserNameChanged = (e)=>{
        setUserName(e.target.value);
    }

    return (
        <ThemeProvider theme={theme}>
            <Grid container component="main" sx={{ height: '100vh' }}>
                <CssBaseline />
                <Grid
                    item
                    xs={false}
                    sm={4}
                    md={7}
                    sx={{
                        backgroundImage: 'url(https://source.unsplash.com/random)',
                        backgroundRepeat: 'no-repeat',
                        backgroundColor: (t) =>
                            t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                    }}
                />
                <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
                    <Box
                        sx={{
                            my: 8,
                            mx: 4,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                            <LockOutlinedIcon />
                        </Avatar>
                        <Typography component="h1" variant="h5">
                            Sign in
                        </Typography>
                        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 1 }}>
                            {alertText && renderAlert(alertText)}
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="userName"
                                label="please enter a unique user name"
                                name="userName"
                                autoComplete="userName"
                                autoFocus
                                onChange = {handleUserNameChanged}
                            />
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{mt: 3, mb: 2}}
                                onClick={() => {
                                }}
                            >
                                Sign In
                            </Button>
                            <Grid container>
                                <Grid item>
                                    <Link href="/signup" variant="body2">
                                        {"Don't have an account? Sign Up"}
                                    </Link>
                                </Grid>
                            </Grid>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}
