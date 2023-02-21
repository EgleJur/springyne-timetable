import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { useParams } from "react-router-dom";


function CreateSubjectPage() {
  const [description, setDescription] = useState("");
  const [name, setName] = useState("");
  const [room, setRoom] = useState("");
  const [nameError, setNameError] = useState("");
  const [moduleError, setModuleError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();
  const [modules, setModules] = useState([]);
  const [selectedModule, setSelectedModule] = useState("");

  const [rooms, setRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState('');

  useEffect(() => {
    fetch('api/v1/modules/')
      .then(response => response.json())
      .then(setModules)

  }, []);

  useEffect(() => {
    fetch('api/v1/rooms/')
      .then(response => response.json())
      .then(setRooms)

  }, []);


  const createNewSubject = (e) => {
    e.preventDefault();
    setNameError(false);
    setModuleError(false);
    if (name === "") {
      setNameError(true);
      setModuleError(true);
    } else {
      fetch(`/api/v1/subjects/createSubject?moduleId=${selectedModule}&roomId=${selectedRoom}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          description,
        })
        ,
      }).then((result) => {
        if (result.ok) {
          setName("");
          setDescription("");
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };


  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują dalyką</h2>
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
          id="create-subject-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />

        <TextField
          onChange={(e) => setDescription(e.target.value)}
          value={description}
          id="create-subject-number-with-error"
          label="Aprašas"
          className="form-control mb-3"
        />
        <label htmlFor="page-size-select" className="mb-3">
          Modulis:
        </label>

        <select
          value={selectedModule}
          // defaultValue={"default"}
          onChange={(e) => setSelectedModule(e.target.value)}
          className={`form-control mb-3 ${selectedModule ? "" : "border-danger"}`}
          required>
          <option value="" disabled>Pasirinkite modulį</option>
          {
            modules.map(
              (mod) =>
              (<option key={mod.id}
                value={mod.id}
                disabled={mod.deleted}>{mod.name}</option>)
            )
          }
        </select>
        {!selectedModule && (
          <div className="form-text text-danger">
            Prašome pasirinkti modulį iš sąrašo.
          </div>
        )}

        <label htmlFor="page-size-select" className="mb-3">
          Kabinetas:
        </label>
        <select
          value={selectedRoom}
          onChange={(e) => setSelectedRoom(e.target.value)}
          className="form-control mb-3">
          <option value=''>---</option>
          {
            rooms.map(
              (room) =>
              (<option key={room.id}
                value={room.id}
                disabled={room.deleted}>{room.name}</option>)
            )
          }
        </select>

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
