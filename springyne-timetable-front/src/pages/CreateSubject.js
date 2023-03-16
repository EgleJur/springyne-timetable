import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { FormControl, MenuItem, Select, InputLabel } from "@mui/material";
import { TextField } from "@mui/material";
import { useParams } from "react-router-dom";
import { apiUrl } from "../App";


function CreateSubjectPage() {
  const [description, setDescription] = useState("");
  const [name, setName] = useState("");
  const [room, setRoom] = useState("");
  const [nameError, setNameError] = useState(false);
  const [moduleError, setModuleError] = useState(false);
  const [roomError, setRoomError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();
  const [modules, setModules] = useState([]);
  const [selectedModule, setSelectedModule] = useState("");
  const [descriptionError, setDescriptionError] = useState(false);
  const [rooms, setRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState('');

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/modules/`)
      .then(response => response.json())
      .then(setModules)

  }, []);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/rooms/`)
      .then(response => response.json())
      .then(setRooms)

  }, []);


  const createNewSubject = (e) => {
    e.preventDefault();
    setNameError(false);
    setModuleError(false);
    setRoomError(false);
    setDescriptionError(false);
    if (name === "" || selectedRoom === ""
      || selectedModule === "" || description === "") {
      if (name === "") { setNameError(true); }
      if (selectedRoom === "") { setRoomError(true); }
      if (selectedModule === "") { setModuleError(true); }
      if (description === "") { setDescriptionError(true);}
    } else {
      fetch(
        `${apiUrl}/api/v1/subjects/createSubject?moduleId=${selectedModule}&roomId=${selectedRoom}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name,
            description,
          }),
        }
      ).then((result) => {
        if (result.ok) {
          setName("");
          setDescription("");
          setSuccess(true);
          setFailure(false);
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
        }
      });
    }
  };


  return (
    <div className="mx-3">
      <h2 className="my-5">
        Pridėti naują dalyką</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
        </Alert>
      </Collapse>

      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>
      <form noValidate>

        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-subject-name-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />

        <TextField
        error={!!descriptionError}
          onChange={(e) => setDescription(e.target.value)}
          value={description}
          id="create-subject-description-with-error"
          label="Aprašymas"
          helperText="Aprašymas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />

        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-module-label" error={moduleError} required>
            Pasirinkite modulį
          </InputLabel>
          <Select
            error={moduleError}
            labelId="select-module-label"
            //nputLabelProps={{ shrink: true }}
            id="add-select-module"
            label="Pasirinkite modulį"
            fullWidth
            value={selectedModule}
            // defaultValue={"default"}
            onChange={(e) => setSelectedModule(e.target.value)}
            required>
            {
              modules?.map((mod) => (
                <MenuItem
                  key={mod.id}
                  value={mod.id}
                  disabled={mod.deleted}
                >
                  {mod.name}
                </MenuItem>
              ))}
          </Select>
        </FormControl>

        <FormControl fullWidth size="small" className="mb-5">
          <InputLabel id="select-room-label" error={roomError} required>
            Pasirinkite kabinetą
          </InputLabel>
          <Select
            error={roomError}
            labelId="select-room-label"
            InputLabelProps={{ shrink: true }}
            id="add-select-room"
            label="Pasirinkite kabinetą"
            fullWidth
            value={selectedRoom}
            onChange={(e) => setSelectedRoom(e.target.value)}
            required>
            {
              rooms?.map((room) => (
                <MenuItem
                  key={room.id}
                  value={room.id}
                  disabled={room.deleted}>{room.name}
                </MenuItem>
              ))}
          </Select>
        </FormControl>

        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewSubject}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateSubjectPage;
