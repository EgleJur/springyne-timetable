import { useEffect, useState } from "react"


export function ModulesForSubjects(props) {
    const [modules, setModules] = useState([]);
    const [selectedModule, setSelectedModule] = useState('');

    useEffect(() => {
        fetch('api/v1/modules/')
            .then(response => response.json())
            .then(setModules)
    }, []);

    // const assignAnimalTomod = () => {
    //     fetch(`api/v1/subjects/test?sudId=${props.id}&modId=${selectedModule}`, {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json'
    //         }
    //         }).then(response=>response.json())
    //         .then((subject)=>props.onModuleChange(subject));
    // };

    return (<div>
        <select
            value={selectedModule}
            onChange={(e) => setSelectedModule(e.target.value)}
            className="form-control mb-3">
            <option value =''>---</option>
            {
                modules.map(
                    (mod) =>
                    (<option key={mod.id} 
                        value={mod.id}>{mod.name}</option>)
                )
            }
        </select>
        {/* <button onClick={assignAnimalTomod}>Asign</button> */}
    </div>)
}