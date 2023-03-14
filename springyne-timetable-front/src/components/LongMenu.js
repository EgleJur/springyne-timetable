import * as React from 'react';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import MoreVertIcon from '@mui/icons-material/MoreVert';

import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from "@mui/material/DialogTitle";
import { FormControl, InputLabel, TextField } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { Select, OutlinedInput } from "@mui/material";
import dayjs from "dayjs";

import FormControlLabel from '@mui/material/FormControlLabel';
import FormLabel from '@mui/material/FormLabel';
import Checkbox from '@mui/material/Checkbox';


const ITEM_HEIGHT = 48;

const LongMenu = ({ color, lesson, lessonId, subjectId, teacherId, roomId, starts, ends }) => {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const openMenu = Boolean(anchorEl);

    const params = useParams();
    const [teacherError, setTeacherError] = useState("");
    const [roomError, setRoomError] = useState("");
    const [selectedSubject, setSelectedSubject] = useState("");
    const [selectedTeacher, setSelectedTeacher] = useState(teacherId);
    const [selectedRoom, setSelectedRoom] = useState(roomId);
    const [teachers, setTeachers] = useState([]);
    const [rooms, setRooms] = useState([]);
    const today = dayjs().format("YYYY-MM-DD");
    const [success, setSuccess] = useState(false);
    const [failure, setFailure] = useState(false);
    const times = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14];
    const [lessons, setLessons] = useState([]);
    const [openEdit, setOpenEdit] = useState(false);
    const [repeats, setRepeats] = useState(false);
    const [showTeacherMenuItem, setShowTeacherMenuItem] = useState(true);
    const [showRoomMenuItem, setShowRoomMenuItem] = useState(true);


    const fetchTeachers = () => {
        fetch(
            `/api/v1/teachers/subject?subjectId=${subjectId}&startTime=${starts}&endTime=${ends}`
        )
            .then((response) => response.json())
            .then((jsonResponse) => setTeachers(jsonResponse));
    }

    useEffect(fetchTeachers, []);

    const prefillRooms = () => {
        setRooms(lesson?.subject?.rooms);
    };

    useEffect(prefillRooms, []);

    const fetchLessons = () => {
        fetch("/api/v1/lessons/schedule/" + params.id)
            .then((response) => response.json())
            .then((jsonResponse) => setLessons(jsonResponse));
    };
    useEffect(fetchLessons, [params]);

    const editLesson = (e) => {
        e.preventDefault();
        fetch(
            `/api/v1/lessons/editSingleLesson/${lessonId}?subjectId=${subjectId}&teacherId=${selectedTeacher}&roomId=${selectedRoom}`,
            {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        ).then((result) => {
            if (result.ok) {
                setRepeats(false);
                setSelectedSubject("");
                setSelectedTeacher("");
                setSelectedRoom("");
                setSuccess(true);
                setFailure(false);
                setOpenEdit(false);
                fetchLessons();
                setTimeout(() => {
                    setSuccess(false);
                }, 5000);
            } else {
                setOpenEdit(false);
                setFailure(true);
                setSuccess(false);
                setTimeout(() => {
                    setFailure(false);
                }, 5000);
            }
        });
    };

    const handleChange = (event) => {
        setRepeats(event.target.checked);
    };

    //const lesson = prop.lessonId;
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };
    // const color = props.color;

    //console.log(lesson);

    return (
        <div>
            <IconButton
                aria-label="more"
                id="long-button"
                aria-controls={openMenu ? 'long-menu' : undefined}
                aria-expanded={openMenu ? 'true' : undefined}
                aria-haspopup="true"
                onClick={handleClick}
                sx={{ bgcolor: color, borderRadius: '0px' }}
            >
                <MoreVertIcon />
            </IconButton>
            <Menu
                id="long-menu"
                MenuListProps={{
                    'aria-labelledby': 'long-button',
                }}
                anchorEl={anchorEl}
                open={openMenu}
                onClose={handleClose}
                PaperProps={{
                    style: {
                        maxHeight: ITEM_HEIGHT * 4.5,
                        width: '12ch',
                    },
                }}
            >
                <MenuItem key='Redaguoti' selected={'Redaguoti' === 'Pyxis'} onClick={() => setOpenEdit(true)}>
                    Redaguoti
                </MenuItem>
                <MenuItem key='Istrinti' selected={'Ištrinti' === 'Pyxis'} onClick={() => setOpenEdit(true)}>
                    Ištrinti
                </MenuItem>
            </Menu>

            <Dialog open={openEdit} onClose={() => setOpenEdit(false)}>
                <DialogTitle className="mt-2 mb-2">Redaguoti pamoką {lesson?.subject?.name}
                </DialogTitle>

                <DialogContent>
                    <DialogContentText className="mt-2 mb-2">
                        {lesson?.lessonDate}
                    </DialogContentText>
                    <form noValidate>

                        <FormControl fullWidth size="small" className="mb-3">
                            <Select
                                error={!!teacherError}
                                labelId="select-teacher-label"
                                id="select-teacher"
                                input={<OutlinedInput notched label="" />}
                                label="Pasirinkite mokytoją"
                                fullWidth
                                value={selectedTeacher}
                                onChange={(e) => setSelectedTeacher(e.target.value)}
                                displayEmpty
                                onOpen={() => {
                                    setShowTeacherMenuItem(false);
                                }}
                                onClose={() => {
                                    setShowTeacherMenuItem(true);
                                }}
                            >
                                <MenuItem
                                    value=""
                                    style={{ display: showTeacherMenuItem ? "block" : "none" }}
                                >
                                    {lesson?.teacher?.name}
                                </MenuItem>
                                {teachers?.map((teacher) => (
                                    <MenuItem
                                        value={teacher.id}
                                        key={teacher.id}
                                        disabled={teacher.deleted}
                                    >
                                        {teacher.name}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        <FormControl fullWidth size="small" className="mb-3">
                            <Select
                                error={!!roomError}
                                labelId="select-room-label"
                                id="select-room"
                                input={<OutlinedInput notched label="" />}
                                label="Pasirinkite kabinetą"
                                fullWidth
                                value={selectedRoom}
                                onChange={(e) => setSelectedRoom(e.target.value)}
                                displayEmpty
                                onOpen={() => {
                                    setShowRoomMenuItem(false);
                                }}
                                onClose={() => {
                                    setShowRoomMenuItem(true);
                                }}
                            >
                                <MenuItem
                                    value=""
                                    style={{ display: showRoomMenuItem ? "block" : "none" }}
                                >
                                    {lesson?.room?.name}
                                </MenuItem>
                                {rooms?.map((room) => (
                                    <MenuItem
                                        value={room.id}
                                        key={room.id}
                                        disabled={room.deleted}
                                    >
                                        {room.name}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </form>
                </DialogContent>
                <DialogActions>
                    <button className="btn btn-primary mb-2" onClick={editLesson}>
                        Redaguoti
                    </button>
                    <button
                        className="btn btn-danger me-3 mb-2"
                        onClick={() => setOpenEdit(false)}
                    >
                        Atšaukti
                    </button>
                </DialogActions>
            </Dialog>

        </div>
    );

}
export default LongMenu;