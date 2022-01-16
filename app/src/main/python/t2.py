import boto3

from datetime import datetime
allms = {} 
lastr = {}
freq={}
def test(a):
##    meds={"panadol":["paracetomol","Pain Relief"],"hovid-paracetamol":["paracetomol","Pain Relief"],"timoptic":["timoptic maleate opthalmic","Eye Soreness"],"timolo":["timoptic maleate opthalmic","Eye Soreness"]}
    meds={"nexium":"esamoprazole","panadol":"paracetomol","hovid-paracetamol":"paracetomol","timoptic":"timoptic maleate opthalmic","timolo":"timoptic maleate opthalmic"}
    meds2={"nexium":"stomach acidity","panadol":"Pain Relief","hovid-paracetamol":"Pain Relief","timoptic":"Eye Soreness","timolo":"Eye Soreness"}
    meds3 = {"nexium":"Must be prescribed by physician","panadol":"2 capsules a day after meal","hovid-paracetamol":"Pain Relief","timoptic":"Eye Soreness","timolo":"Eye Soreness"}
    meds4 = {"nexium":"headache, diarrhea, nausea","panadol":"Diarrhea, increased sweating, nausea","hovid-paracetamol":"Pain Relief","timoptic":"Eye Soreness","timolo":"Eye Soreness"}
    
    newa = a.split(" ")
    outs = ""
    for elems in newa:
        if (elems.lower() in meds) and (elems.lower() not in outs):
            outs+="Brand name: "
            outs+=elems.lower()
            allms[elems.lower()] = "ok"
            outs+="   .Contents: "
            outs+=meds[elems.lower()]
            if elems.lower() in lastr:
                outs+=". Last Eaten"
                outs+=lastr[elems.lower()]
            else:
                outs+=". Last Eaten: No prior records of this medication"

            if elems.lower() in freq:
                outs+=". Frequency: Eat once every "
                outs+= freq[elems.lower()] + " days"
            else:
                outs+=". Frequency: Has not been set by user "
                
            outs+=". Common Uses: "
            outs+=meds2[elems.lower()]
            outs+=". Dosage: "
            outs+=meds3[elems.lower()]
            outs+=". Side Effects: "
            outs+=meds4[elems.lower()]
            
##            h = datetime.now()
##            lastr[elems.lower()] = h.strftime("%d") +" "+h.strftime("%B")
##            lastr[elems.lower()]=h    
            
        if ("mg" in elems) or ("%" in elems) and ("Quantity" not in outs):
            outs+=". Quantity: "
            outs+=elems
        
            
    # Find details on entities found
    

    return outs


def eat(q):
    newb = q.split(" ")
    m="None Found"
    for elems in newb:
        if (elems.lower()) in allms:
            m=elems.lower()
            h = datetime.now()
            lastr[elems.lower()] = h.strftime("%d") +" "+h.strftime("%B") + ". Time: "+ h.strftime("%H")+":"+h.strftime("%M")
    return q
            

def reminder(medname,w):
    numd = w
    newc = medname.split(" ")
    for elems in newc:
        if (elems.lower()) in allms:
            freq[elems.lower()] = numd
    return medname
            
    
