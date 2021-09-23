import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import {List, ListItemAvatar} from "@material-ui/core";
import Avatar from "@mui/material/Avatar";
import PersonOutlinedIcon from '@mui/icons-material/PersonOutlined';

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
        height: 400,
        maxWidth: 300,
        backgroundColor: theme.palette.background.paper,
    },
}));


export default function UserList({users}) {
    const classes = useStyles();
    console.log("in user list ");
    console.log({users});
    return (
        <div className={classes.root}>
            <List>
                {users.map(user => {
                        return (<ListItem key = {user}>
                            <ListItemAvatar>
                                <Avatar>
                                    <PersonOutlinedIcon/>
                                </Avatar>
                            </ListItemAvatar>
                            <ListItemText
                                primary={console.log(user)||user}
                            />
                        </ListItem>)
                    }
                )}
            </List>
        </div>
    );
}
