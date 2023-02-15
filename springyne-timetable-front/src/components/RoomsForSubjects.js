import { useEffect, useState } from "react"


export function RoomsForSubjects(props) {
    const [rooms, setRooms] = useState([]);
    const [selectedRoom, setSelectedRoom] = useState('');

    useEffect(() => {
        fetch('api/v1/rooms/')
            .then(response => response.json())
            .then(setRooms)
    }, []);

    // const assignAnimalToroom = () => {
    //     fetch(`api/v1/animals/${props.id}/addroom?roomId=${selectedRoom}`, {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json'
    //         }
    //         }).then(response=>response.json())
    //         .then((animal)=>props.onAnimalChange(animal));
    // };

    return (<div>
        <select
            value={selectedRoom}
            onChange={(e) => setSelectedRoom(e.target.value)}
            className="form-control mb-3">
            <option value =''>---</option>
            {
                rooms.map(
                    (room) =>
                    (<option key={room.id} 
                        value={room.id}>{room.name}</option>)
                )
            }
        </select>
        {/* <button onClick={assignAnimalToroom}>Asign</button> */}
    </div>)
}