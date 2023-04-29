package com.company.certificate.service;


import com.company.certificate.exception.BadRequest;
import com.company.certificate.model.Certificate;
import com.company.certificate.repository.CertificateRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DirectionService directionService;

    @Value("${director}")
    private String directorName;

    private String assets = System.getProperty("user.dir") + "/assets/";

    public boolean create(Certificate certificate) {
        String token = UUID.randomUUID().toString();
        String YMD = getYMD();
        String urlQR = "http://localhost:8080/certificates/" + token;
        String url = "http://localhost:8080/api/v1/certificates/get" + token;

        System.out.println(assets);

        BufferedImage QR = generateQR(urlQR);

        String path = assets + YMD + "/" + token + ".png";

        File folder = new File(assets + YMD);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        certificate.setUser(userService.getById(certificate.getUserId()));
        certificate.setDirection(directionService.getById(certificate.getDirectionId()));
        // RASMGA OZGARTIRILISHLA KIRITILINADI
        generateImage(QR, certificate, path);

        //DB save
        saveCertificateToDb(token, url, urlQR, path, certificate);

        return true;
    }

    private void saveCertificateToDb(String token, String url, String urlQR, String path, Certificate certificate) {
        certificate.setToken(token);
        certificate.setUrl(url);
        certificate.setPath(path);
        certificate.setUrlQR(urlQR);
        certificateRepository.save(certificate);
    }

    public void generateImage(BufferedImage qr, Certificate certificate, String path) {
        String originalPath = assets + "/img/certificate.jpg";
        BufferedImage image;
        try {
            image = ImageIO.read(new File(originalPath));
            Graphics2D graphics = image.createGraphics();

            int x;
            int y;

            Font font = new Font("Serif", Font.BOLD, 100); // 120
            graphics.setFont(font);
            graphics.setColor(Color.ORANGE);
            String firstname = certificate.getUser().getFirstname();
            String lastname = certificate.getUser().getLastname();

            FontMetrics fontMetrics = graphics.getFontMetrics(font);
            Rectangle rectangle = new Rectangle();
            rectangle.x = 500; // 1000
            rectangle.y = 525; // 1250
            rectangle.width = 1000; // 2400
            rectangle.height = 225; // 320
            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(firstname)) / 2;
            y = rectangle.y + rectangle.height - 120;
            graphics.drawString(firstname, x, y);
            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(lastname)) / 2;
            y = rectangle.y + rectangle.height;
            graphics.drawString(lastname, x, y);

            // * * * * *

            font = new Font("Serif", Font.BOLD, 65);
            graphics.setFont(font);
            graphics.setColor(Color.BLACK);
            fontMetrics = graphics.getFontMetrics(font);

            rectangle = new Rectangle();
            rectangle.x = 965; // 2800
            rectangle.y = 960; // 2066
            rectangle.width = 620; // 650
            rectangle.height = 200; // 200

            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(directorName)) / 2;
            y = rectangle.y + rectangle.height - 50;

            graphics.drawString(directorName, x, y);

            //

            font = new Font("Serif", Font.BOLD, 90); //90
            graphics.setFont(font);
            graphics.setColor(Color.ORANGE);
            fontMetrics = graphics.getFontMetrics(font);

            String direction = certificate.getDirection().getName();

            rectangle = new Rectangle();
            rectangle.x = 700; // 1750
            rectangle.y = 800; // 2066
            rectangle.width = 580; // 650
            rectangle.height = 200; // 200

            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(direction)) / 2;
            y = rectangle.y + rectangle.height - 50;

            graphics.drawString(direction, x, y);

            graphics.drawImage(qr, 1550, 650, qr.getWidth(), qr.getHeight(), null);

            graphics.dispose();

            File file = new File(path);
            ImageIO.write(image, "png", file);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // QR rasm yaratish uchun funksiya
    public BufferedImage generateQR(String url) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 270, 270);
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", new File(assets + "qr" + ".png"));
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getYMD() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return String.format("%s/%s/%s", year, month + 1, day);
    }

    public byte[] get(String token) {
        try {
            Certificate entity = getCertificate(token);
            String imagePath = entity.getPath();

            byte[] imageInByte;
            BufferedImage originalImage;
            try {
                originalImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                return new byte[0];
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private Certificate getCertificate(String token) {
        Optional<Certificate> optional = certificateRepository.findByTokenAndDeletedAtIsNull(token);
        if (optional.isEmpty()) {
            throw new BadRequest("Certificate not found");
        }
        return optional.get();
    }

    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }
}
