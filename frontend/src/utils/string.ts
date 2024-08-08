import axios from "axios";
import { API_BASE_URL } from "./fetcher";
import { toBase64 } from "./file";

export const clsx = (...classes: (string | false | null | undefined)[]) => {
  return classes.filter((cls): cls is string => !!cls).join(" ");
};

export const isNotBlank = (val: any): val is string =>
  typeof val === "string" && val.trim().length > 0;

export const isBlank = (val: any) => !isNotBlank(val);

export const convertToDatePickerFormat = (date: Date | string) => {
  if (typeof date === "string") {
    return date.split("/").reverse().join("-");
  } else {
    return date.toLocaleDateString("sv");
  }
};

export const convertBase64ToBlob = (base64Image: string) => {
  // Split into two parts
  const parts = base64Image.split(";base64,");
  // Hold the content type
  const imageType = parts[0].split(":")[1];
  // Decode Base64 string
  const decodedData = window.atob(parts[1]);
  // Create UNIT8ARRAY of size same as row data length
  const uInt8Array = new Uint8Array(decodedData.length);
  // Insert all character code into uInt8Array
  for (let i = 0; i < decodedData.length; ++i) {
    uInt8Array[i] = decodedData.charCodeAt(i);
  }
  // Return BLOB image after conversion
  return new Blob([uInt8Array], { type: imageType });
};

export const getFileFromPath = async (filePath: string, fileName: string) => {
  const response = await axios.get(`${API_BASE_URL}/files/docs/${filePath}`, {
    responseType: "blob",
  });
  let file = new File([response.data], fileName, {
    type: "application/pdf",
  });

  return file;
};

export const getBase64FromPath = async (filePath: string, fileName: string) => {
  const file = await getFileFromPath(filePath, fileName);
  const base64 = await toBase64(file);
  return base64;
};

export const getImageFromPath = async (filePath: string, fileName: string) => {
  const response = await axios.get(`${API_BASE_URL}/files/imgs/${filePath}`, {
    responseType: "blob",
  });
  const file = new File([response.data], fileName, {
    type: "image/png",
  });
  return file;
};

const validFileExtensions: any = {
  image: ["jpg", "png", "jpeg"],
  pdf: ["pdf"],
};

export const isValidFileType = (fileName: any, fileType: any) => {
  let result;

  if (fileName && fileType === "image") {
    result =
      validFileExtensions[fileType].indexOf(fileName.split(".").pop()) > -1;
  } else if (fileName && fileType === "pdf") {
    result =
      fileName.split(".").length < 3 &&
      validFileExtensions[fileType].indexOf(fileName.split(".").pop()) > -1;
  }
  return result;
};
export const readableDateFrom = (timestamp: Date | number | string) =>
  timestamp
    ? new Date(timestamp).toLocaleDateString("en-IN", {
        timeZone: "Asia/Kolkata",
        day: "2-digit",
        month: "short",
        year: "numeric",
      })
    : "";

export const dateDifferenceInDays = (
  startDate: Date | any,
  endDate: Date | any
) => {
  const date1: any = new Date(startDate);
  const date2: any = new Date(endDate);
  const diffTime: any = Math.abs(date2 - date1);
  const diffDays: any = Math.floor(diffTime / (1000 * 60 * 60 * 24) + 1);

  return diffDays;
};

export const refreshOnSpecificTime = (dateToRefresh: any) => {
  const currDate: any = new Date();
  const checkDate: any = new Date(dateToRefresh);
  if (currDate < checkDate) {
    let diff = checkDate - currDate;

    const timeoutId = setTimeout(() => {
      window.location.reload();
    }, diff);
  }
};

export const callApiAtSpecificInterval = (
  functionToCall: () => any,
  interval: number
) => {
  const intervalId = setInterval(() => {
    functionToCall();
  }, interval);
};
