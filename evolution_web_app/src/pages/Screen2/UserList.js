import React from 'react';
import {makeStyles} from '@mui/styles';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import {List, ListItemAvatar} from "@mui/material";
import Avatar from "@mui/material/Avatar";
import PersonOutlinedIcon from '@mui/icons-material/PersonOutlined';

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
        height: 'auto',
        maxWidth: 300,
        backgroundColor: 'lightblue',
    },
    icon: {
        backgroundColor: 'lightpink',
        color: 'lightblue',
    },
}));


export default function UserList({users}) {
    const classes = useStyles();
    return (
        <div className={classes.root}>
            <List>
                {users.map(user => {
                        return (<ListItem key={user}>
                            <ListItemAvatar>
                                <Avatar className={classes.icon}>
                                    <PersonOutlinedIcon/>
                                </Avatar>
                            </ListItemAvatar>
                            <ListItemText
                                primary={user}
                            />
                        </ListItem>)
                    }
                )}
            </List>
        </div>
    );
}
